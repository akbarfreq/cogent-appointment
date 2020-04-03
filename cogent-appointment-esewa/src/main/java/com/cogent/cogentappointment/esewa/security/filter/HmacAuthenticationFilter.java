package com.cogent.cogentappointment.esewa.security.filter;

import com.cogent.cogentappointment.esewa.dto.request.admin.AdminMinDetails;
import com.cogent.cogentappointment.esewa.dto.request.login.ThirdPartyDetail;
import com.cogent.cogentappointment.esewa.repository.HmacApiInfoRepository;
import com.cogent.cogentappointment.esewa.security.hmac.AuthHeader;
import com.cogent.cogentappointment.esewa.security.hmac.HMACBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cogent.cogentappointment.esewa.constants.ErrorMessageConstants.HMAC_BAD_SIGNATURE;
import static com.cogent.cogentappointment.esewa.constants.PatternConstants.AUTHORIZATION_HEADER_PATTERN;
import static com.cogent.cogentappointment.esewa.constants.PatternConstants.AUTHORIZATION_HEADER_PATTERN_FOR_ESEWA;

/**
 * @author Sauravi Thapa २०/१/१९
 */
@Component
public class HmacAuthenticationFilter extends OncePerRequestFilter {

    private final HmacApiInfoRepository hmacApiInfoRepository;

    public HmacAuthenticationFilter(HmacApiInfoRepository hmacApiInfoRepository) {
        this.hmacApiInfoRepository = hmacApiInfoRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final AuthHeader eSewaAuthHeader = getAuthHeaderForeSewa(request);


        if (eSewaAuthHeader != null) {

            ThirdPartyDetail thirdPartyDetail = hmacApiInfoRepository.getDetailForAuthentication(
                    eSewaAuthHeader.getCompanyCode(),
                    eSewaAuthHeader.getApiKey());

            final HMACBuilder signatureBuilder = new HMACBuilder()
                    .algorithm(eSewaAuthHeader.getAlgorithm())
                    .nonce(eSewaAuthHeader.getNonce())
                    .companyCode(eSewaAuthHeader.getCompanyCode())
                    .apiKey(eSewaAuthHeader.getApiKey())
                    .apiSecret(thirdPartyDetail.getApiSecret());

            compareSignature(signatureBuilder, eSewaAuthHeader.getDigest());

            SecurityContextHolder.getContext().setAuthentication(getAuthentication(thirdPartyDetail.getHospitalCode(),
                    thirdPartyDetail.getHospitalCode()));
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }


    public AuthHeader getAuthHeaderForeSewa(HttpServletRequest request) {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) {
            return null;
        }
        final Matcher authHeaderMatcher = Pattern.compile(AUTHORIZATION_HEADER_PATTERN_FOR_ESEWA).matcher(authHeader);
        if (!authHeaderMatcher.matches()) {
            return null;
        }
        return new AuthHeader(authHeaderMatcher.group(1),
                authHeaderMatcher.group(2),
                authHeaderMatcher.group(3),
                authHeaderMatcher.group(4),
                DatatypeConverter.parseBase64Binary(authHeaderMatcher.group(5)));
    }

    public void compareSignature(HMACBuilder signatureBuilder, byte[] digest) {
        if (!signatureBuilder.isHashEquals(digest))
            throw new BadCredentialsException(HMAC_BAD_SIGNATURE);
    }

    public PreAuthenticatedAuthenticationToken getAuthentication(String username, String hospitalCode) {
        return new PreAuthenticatedAuthenticationToken(
                username,
                hospitalCode,
                null);
    }

    public PreAuthenticatedAuthenticationToken getAuthenticationForHospital(String username, Long hospitalId) {
        return new PreAuthenticatedAuthenticationToken(
                username,
                hospitalId,
                null);
    }
}
