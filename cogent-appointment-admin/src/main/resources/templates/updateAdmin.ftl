<!--
==================== Respmail ====================
Respmail is a response HTML email designed to work
on all major devices and responsive for smartphones
that support media queries.

** NOTE **
This template comes with a lot of standard features
that has been thoroughly tested on major platforms
and devices, it is extremely flexible to use and
can be easily customized by removing any row that
you do not need.

it is gauranteed to work 95% without any major flaws,
any changes or adjustments should thoroughly be
tested and reviewed to match with the general
structure.

** Profile **
Licensed under MIT (https://github.com/charlesmudy/responsive-html-email-template/blob/master/LICENSE)
Designed by Shina Charles Memud
Respmail v1.2 (http://charlesmudy.com/respmail/)

** Quick modification **
We are using width of 500 for the whole content,
you can change it any size you want (e.g. 600).
The fastest and safest way is to use find & replace
Sizes: [
		wrapper   : '500',
		columns   : '210',
		x-columns : [
						left : '90',
						right: '350'
				]
		}
	-->

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="format-detection" content="telephone=no"/> <!-- disable auto telephone linking in iOS -->
    <title>Respmail is a response HTML email designed to work on all major email platforms and smartphones</title>
    <style type="text/css">
        /* RESET STYLES */
        html {
            background-color: #E1E1E1;
            margin: 0;
            padding: 0;
        }

        body, #bodyTable, #bodyCell, #bodyCell {
            height: 100% !important;
            margin: 0;
            padding: 0;
            width: 100% !important;
            font-family: Helvetica, Arial, "Lucida Grande", sans-serif;
        }

        table {
            border-collapse: collapse;
        }

        table[id=bodyTable] {
            width: 100% !important;
            margin: auto;
            max-width: 500px !important;
            color: #7A7A7A;
            font-weight: normal;
        }

        img, a img {
            border: 0;
            outline: none;
            text-decoration: none;
            height: auto;
            line-height: 100%;
        }

        a {
            text-decoration: none !important;
            border-bottom: 1px solid;
        }

        h1, h2, h3, h4, h5, h6 {
            color: #5F5F5F;
            font-weight: normal;
            font-family: Helvetica;
            font-size: 20px;
            line-height: 125%;
            text-align: Left;
            letter-spacing: normal;
            margin-top: 0;
            margin-right: 0;
            margin-bottom: 10px;
            margin-left: 0;
            padding-top: 0;
            padding-bottom: 0;
            padding-left: 0;
            padding-right: 0;
        }

        /* CLIENT-SPECIFIC STYLES */
        .ReadMsgBody {
            width: 100%;
        }

        .ExternalClass {
            width: 100%;
        }

        /* Force Hotmail/Outlook.com to display emails at full width. */
        .ExternalClass, .ExternalClass p, .ExternalClass span, .ExternalClass font, .ExternalClass td, .ExternalClass div {
            line-height: 100%;
        }

        /* Force Hotmail/Outlook.com to display line heights normally. */
        table, td {
            mso-table-lspace: 0pt;
            mso-table-rspace: 0pt;
        }

        /* Remove spacing between tables in Outlook 2007 and up. */
        #outlook a {
            padding: 0;
        }

        /* Force Outlook 2007 and up to provide a "view in browser" message. */
        img {
            -ms-interpolation-mode: bicubic;
            display: block;
            outline: none;
            text-decoration: none;
        }

        /* Force IE to smoothly render resized images. */
        body, table, td, p, a, li, blockquote {
            -ms-text-size-adjust: 100%;
            -webkit-text-size-adjust: 100%;
            font-weight: normal !important;
            border-radius: 4px;
        }

        /* Prevent Windows- and Webkit-based mobile platforms from changing declared text sizes. */
        .ExternalClass td[class="ecxflexibleContainerBox"] h3 {
            padding-top: 10px !important;
        }

        /* Force hotmail to push 2-grid sub headers down */

        /* /\/\/\/\/\/\/\/\/ TEMPLATE STYLES /\/\/\/\/\/\/\/\/ */

        /* ========== Page Styles ========== */
        h1 {
            display: block;
            font-size: 26px;
            font-style: normal;
            font-weight: normal;
            line-height: 100%;
        }

        h2 {
            display: block;
            font-size: 20px;
            font-style: normal;
            font-weight: normal;
            line-height: 120%;
        }

        h3 {
            display: block;
            font-size: 17px;
            font-style: normal;
            font-weight: normal;
            line-height: 110%;
        }

        h4 {
            display: block;
            font-size: 18px;
            font-style: italic;
            font-weight: normal;
            line-height: 100%;
        }

        .flexibleImage {
            height: auto;
        }

        .linkRemoveBorder {
            border-bottom: 0 !important;
        }

        table[class=flexibleContainerCellDivider] {
            padding-bottom: 0 !important;
            padding-top: 0 !important;
        }

        body, #bodyTable {
            background-color: #E1E1E1;
        }

        #emailHeader {
            background-color: #E1E1E1;
        }

        #emailBody {
            background-color: #FFFFFF;
        }

        #emailFooter {
            background-color: #E1E1E1;
        }

        .nestedContainer {
            background-color: #F8F8F8;
            border: 1px solid #CCCCCC;
        }

        .emailButton {
            background-color: #205478;
            border-collapse: separate;
        }

        .buttonContent {
            color: #FFFFFF;
            font-family: Helvetica;
            font-size: 18px;
            font-weight: bold;
            line-height: 100%;
            padding: 15px;
            text-align: center;
        }

        .buttonContent a {
            color: #FFFFFF;
            display: block;
            text-decoration: none !important;
            border: 0 !important;
        }

        .emailCalendar {
            background-color: #FFFFFF;
            border: 1px solid #CCCCCC;
        }

        .emailCalendarMonth {
            background-color: #205478;
            color: #FFFFFF;
            font-family: Helvetica, Arial, sans-serif;
            font-size: 16px;
            font-weight: bold;
            padding-top: 10px;
            padding-bottom: 10px;
            text-align: center;
        }

        .emailCalendarDay {
            color: #205478;
            font-family: Helvetica, Arial, sans-serif;
            font-size: 60px;
            font-weight: bold;
            line-height: 100%;
            padding-top: 20px;
            padding-bottom: 20px;
            text-align: center;
        }

        .imageContentText {
            margin-top: 10px;
            line-height: 0;
        }

        .imageContentText a {
            line-height: 0;
        }

        #invisibleIntroduction {
            display: none !important;
        }

        /* Removing the introduction text from the view */

        /*FRAMEWORK HACKS & OVERRIDES */
        span[class=ios-color-hack] a {
            color: #275100 !important;
            text-decoration: none !important;
        }

        /* Remove all link colors in IOS (below are duplicates based on the color preference) */
        span[class=ios-color-hack2] a {
            color: #205478 !important;
            text-decoration: none !important;
        }

        span[class=ios-color-hack3] a {
            color: #8B8B8B !important;
            text-decoration: none !important;
        }

        /* A nice and clean way to target phone numbers you want clickable and avoid a mobile phone from linking other numbers that look like, but are not phone numbers.  Use these two blocks of code to "unstyle" any numbers that may be linked.  The second block gives you a class to apply with a span tag to the numbers you would like linked and styled.
        Inspired by Campaign Monitor's article on using phone numbers in email: http://www.campaignmonitor.com/blog/post/3571/using-phone-numbers-in-html-email/.
        */
        .a[href^="tel"], a[href^="sms"] {
            text-decoration: none !important;
            color: #606060 !important;
            pointer-events: none !important;
            cursor: default !important;
        }

        .mobile_link a[href^="tel"], .mobile_link a[href^="sms"] {
            text-decoration: none !important;
            color: #606060 !important;
            pointer-events: auto !important;
            cursor: default !important;
        }

        /* MOBILE STYLES */
        @media only screen and (max-width: 480px) {
            /*////// CLIENT-SPECIFIC STYLES //////*/
            body {
                width: 100% !important;
                min-width: 100% !important;
            }

            /* Force iOS Mail to render the email at full width. */
            /* FRAMEWORK STYLES */
            /*
            CSS selectors are written in attribute
            selector format to prevent Yahoo Mail
            from rendering media query styles on
            desktop.
            */
            /*td[class="textContent"], td[class="flexibleContainerCell"] { width: 100%; padding-left: 10px !important; padding-right: 10px !important; }*/
            table[id="emailHeader"],
            table[id="emailBody"],
            table[id="emailFooter"],
            table[class="flexibleContainer"],
            td[class="flexibleContainerCell"] {
                width: 100% !important;
            }

            td[class="flexibleContainerBox"], td[class="flexibleContainerBox"] table {
                display: block;
                width: 100%;
                text-align: left;
            }

            /*
            The following style rule makes any
            image classed with 'flexibleImage'
            fluid when the query activates.
            Make sure you add an inline max-width
            to those images to prevent them
            from blowing out.
            */
            td[class="imageContent"] img {
                height: auto !important;
                width: 100% !important;
                max-width: 100% !important;
            }

            img[class="flexibleImage"] {
                height: auto !important;
                width: 100% !important;
                max-width: 100% !important;
            }

            img[class="flexibleImageSmall"] {
                height: auto !important;
                width: auto !important;
            }

            /*
            Create top space for every second element in a block
            */
            table[class="flexibleContainerBoxNext"] {
                padding-top: 10px !important;
            }

            /*
            Make buttons in the email span the
            full width of their container, allowing
            for left- or right-handed ease of use.
            */
            table[class="emailButton"] {
                width: 100% !important;
            }

            td[class="buttonContent"] {
                padding: 0 !important;
            }

            td[class="buttonContent"] a {
                padding: 15px !important;
            }

        }

        /*  CONDITIONS FOR ANDROID DEVICES ONLY
        *   http://developer.android.com/guide/webapps/targeting.html
        *   http://pugetworks.com/2011/04/css-media-queries-for-targeting-different-mobile-devices/ ;
        =====================================================*/

        @media only screen and (-webkit-device-pixel-ratio: .75) {
            /* Put CSS for low density (ldpi) Android layouts in here */
        }

        @media only screen and (-webkit-device-pixel-ratio: 1) {
            /* Put CSS for medium density (mdpi) Android layouts in here */
        }

        @media only screen and (-webkit-device-pixel-ratio: 1.5) {
            /* Put CSS for high density (hdpi) Android layouts in here */
        }

        /* end Android targeting */

        /* CONDITIONS FOR IOS DEVICES ONLY
        =====================================================*/
        @media only screen and (min-device-width: 320px) and (max-device-width: 568px) {

        }

        /* end IOS targeting */
    </style>
    <!--
        Outlook Conditional CSS

        These two style blocks target Outlook 2007 & 2010 specifically, forcing
        columns into a single vertical stack as on mobile clients. This is
        primarily done to avoid the 'page break bug' and is optional.

        More information here:
        http://templates.mailchimp.com/development/css/outlook-conditional-css
    -->
    <!--[if mso 12]>
    <style type="text/css">
        .flexibleContainer {
            display: block !important;
            width: 100% !important;
        }
    </style>
    <![endif]-->
    <!--[if mso 14]>
    <style type="text/css">
        .flexibleContainer {
            display: block !important;
            width: 100% !important;
        }
    </style>
    <![endif]-->
</head>
<body bgcolor="#E1E1E1" leftmargin="0" marginwidth="0" topmargin="0" marginheight="0" offset="0">

<!-- CENTER THE EMAIL // -->
<!--
1.  The center tag should normally put all the
    content in the middle of the email page.
    I added "table-layout: fixed;" style to force
    yahoomail which by default put the content left.

2.  For hotmail and yahoomail, the contents of
    the email starts from this center, so we try to
    apply necessary styling e.g. background-color.
-->
<center style="background-color:#E1E1E1;">
    <table border="0" cellpadding="0" cellspacing="0" height="100%" width="100%" id="bodyTable"
           style="table-layout: fixed;max-width:100% !important;width: 100% !important;min-width: 100% !important;">
        <tr>
            <td align="center" valign="top" id="bodyCell">

                <!-- EMAIL HEADER // -->
                <!--
                    The table "emailBody" is the email's container.
                    Its width can be set to 100% for a color band
                    that spans the width of the page.
                -->
                <table bgcolor="#E1E1E1" border="0" cellpadding="0" cellspacing="0" width="500" id="emailHeader">

                    <!-- HEADER ROW // -->
                    <tr>
                        <td align="center" valign="top">
                            <!-- CENTERING TABLE // -->
                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                <tr>
                                    <td align="center" valign="top">
                                        <!-- FLEXIBLE CONTAINER // -->
                                        <table border="0" cellpadding="10" cellspacing="0" width="500"
                                               class="flexibleContainer">
                                            <tr>
                                                <td valign="top" width="500" class="flexibleContainerCell">

                                                    <!-- CONTENT TABLE // -->
                                                    <table align="left" border="0" cellpadding="0" cellspacing="0"
                                                           width="100%">
                                                        <tr>
                                                            <!--
                                                                The "invisibleIntroduction" is the text used for short preview
                                                                of the email before the user opens it (50 characters max). Sometimes,
                                                                you do not want to show this message depending on your design but this
                                                                text is highly recommended.

                                                                You do not have to worry if it is hidden, the next <td> will automatically
                                                                center and apply to the width 100% and also shrink to 50% if the first <td>
                                                                is visible.
                                                            -->
                                                            <td align="left" valign="middle" id="invisibleIntroduction"
                                                                class="flexibleContainerBox"
                                                                style="display:none !important; mso-hide:all;">
                                                                <table border="0" cellpadding="0" cellspacing="0"
                                                                       width="100%" style="max-width:100%;">
                                                                    <tr>
                                                                        <td align="left" class="textContent">
                                                                            <div style="font-family:Helvetica,Arial,sans-serif;font-size:13px;color:#828282;text-align:center;line-height:120%;">
                                                                                The introduction of your message preview
                                                                                goes here. Try to make it short.
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                            <td align="right" valign="middle"
                                                                class="flexibleContainerBox">
                                                                <table border="0" cellpadding="0" cellspacing="0"
                                                                       width="100%" style="max-width:100%;">
                                                                    <tr>
                                                                        <td align="left" class="textContent">
                                                                            <!-- CONTENT // -->
                                                                            <div style="font-family:Helvetica,Arial,sans-serif;font-size:13px;color:#828282;text-align:center;line-height:120%;">
                                                                                &nbsp;
                                                                                <!-- If you can't see this message, <a href="#" target="_blank" style="text-decoration:none;border-bottom:1px solid #828282;color:#828282;"><span style="color:#828282;">view&nbsp;it&nbsp;in&nbsp;your&nbsp;browser</span></a>. -->
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // FLEXIBLE CONTAINER -->
                                    </td>
                                </tr>
                            </table>
                            <!-- // CENTERING TABLE -->
                        </td>
                    </tr>
                    <!-- // END -->

                </table>
                <!-- // END -->

                <!-- EMAIL BODY // -->
                <!--
                    The table "emailBody" is the email's container.
                    Its width can be set to 100% for a color band
                    that spans the width of the page.
                -->
                <table bgcolor="#FFFFFF" border="0" cellpadding="0" cellspacing="0" width="500" id="emailBody">

                    <!-- MODULE ROW // -->
                    <!--
                        To move or duplicate any of the design patterns
                        in this email, simply move or copy the entire
                        MODULE ROW section for each content block.
                    -->
                    <tr>
                        <td align="center" valign="top">
                            <!-- CENTERING TABLE // -->
                            <!--
                                The centering table keeps the content
                                tables centered in the emailBody table,
                                in case its width is set to 100%.
                            -->
                            <table border="0" cellpadding="0" cellspacing="0" width="100%"
                                   style="color:#FFFFFF;border-bottom-left-radius: 0px;border-bottom-right-radius:0;"
                                   bgcolor="#007bff">
                                <tr>
                                    <td align="center" valign="top">
                                        <!-- FLEXIBLE CONTAINER // -->
                                        <!--
                                            The flexible container has a set width
                                            that gets overridden by the media query.
                                            Most content tables within can then be
                                            given 100% widths.
                                        -->
                                        <table border="0" cellpadding="0" cellspacing="0" width="500"
                                               class="flexibleContainer">
                                            <tr>
                                                <td align="center" valign="top" width="500"
                                                    class="flexibleContainerCell">

                                                    <!-- CONTENT TABLE // -->
                                                    <!--
                                                    The content table is the first element
                                                        that's entirely separate from the structural
                                                        framework of the email.
                                                    -->
                                                    <table border="0" cellpadding="30" cellspacing="0" width="100%">
                                                        <tr>
                                                            <td align="center" valign="top" class="textContent">
                                                                <h1 style="color:#FFFFFF;line-height:100%;font-family:Helvetica,Arial,sans-serif;font-size:35px;font-weight:normal;margin-bottom:5px;text-align:center;">
                                                                    Admin Details</h1>
                                                                <!-- <h2 style="text-align:center;font-weight:normal;font-family:Helvetica,Arial,sans-serif;font-size:23px;margin-bottom:10px;color:#205478;line-height:135%;">Subheader introduction</h2>
                                                                <div style="text-align:center;font-family:Helvetica,Arial,sans-serif;font-size:15px;margin-bottom:0;color:#FFFFFF;line-height:135%;">Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante.</div> -->
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    <!-- // CONTENT TABLE -->

                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // FLEXIBLE CONTAINER -->
                                    </td>
                                </tr>
                            </table>
                            <!-- // CENTERING TABLE -->
                        </td>
                    </tr>
                    <!-- // MODULE ROW -->


                    <!-- MODULE ROW // -->
                    <tr>
                        <td align="center" valign="top">
                            <!-- CENTERING TABLE // -->
                            <table border="0" cellpadding="0" cellspacing="0" width="100%" bgcolor="#fff">
                                <tr>
                                    <td align="center" valign="top" style="padding-top:40px;padding-bottom:10px;">
                                        <!-- FLEXIBLE CONTAINER // -->
                                        <table border="0" cellpadding="0" cellspacing="0" width="500"
                                               class="flexibleContainer">
                                            <tr>
                                                <td align="center" valign="top" width="500"
                                                    class="flexibleContainerCell">
                                                    <table border="0" cellpadding="30" cellspacing="0" width="100%">
                                                        <tr>
                                                            <td align="center" valign="top">

                                                                <!-- CONTENT TABLE // -->
                                                                <table border="0" cellpadding="0" cellspacing="0"
                                                                       width="100%">
                                                                    <tr>
                                                                        <td valign="top" class="textContent">
                                                                            <!--
                                                                                The "mc:edit" is a feature for MailChimp which allows
                                                                                you to edit certain row. It makes it easy for you to quickly edit row sections.
                                                                                http://kb.mailchimp.com/templates/code/create-editable-content-areas-with-mailchimps-template-language
                                                                            -->
                                                                            <h3 mc:edit="header"
                                                                                style="color:#101010;line-height:125%;font-family:Helvetica,Arial,sans-serif;font-size:20px;font-weight:normal;margin-top:0;margin-bottom:10px;text-align:left;">
                                                                                Dear Mr/Mrs/Miss [username]</h3>
                                                                            <div mc:edit="body"
                                                                                 style="text-align:left;font-family:Helvetica,Arial,sans-serif;font-size:15px;margin-bottom:0;color:#5F5F5F;line-height:135%;">
                                                                                Your following details has been updated
                                                                                :
                                                                            </div>
                                                                            <ul style="margin-top:20px; padding:0;text-align:left;font-family:Helvetica,Arial,sans-serif;font-size:15px;margin-bottom:0;color:#5F5F5F;">


                                                                                <li style="Margin:0 0 5px; list-style:none inside; mso-special-format:bullet;">
                                                                                    <span style="font-weight:bold;">Name :</span>
                                                                                    [Item one]
                                                                                </li>
                                                                                <li style="Margin:0 0 5px; list-style:none inside; mso-special-format:bullet;">
                                                                                    <span style="font-weight:bold;"> Mobile No :</span>
                                                                                    [9876234234]
                                                                                </li>

                                                                            <#list updatedData as data>
                                                                                <li> ${data}</li>
                                                                            </#list>
                                                                            </ul>


                                                                            <div mc:edit="body"
                                                                                 style="text-align:left;font-family:Helvetica,Arial,sans-serif;font-size:15px;margin-bottom:0;color:#5F5F5F;line-height:135%;margin-top:30px;">
                                                                                Mac Address :
                                                                            </div>

                                                                            <ul style="margin-top:10px; padding:0;text-align:left;font-family:Helvetica,Arial,sans-serif;font-size:15px;margin-bottom:0;color:#5F5F5F;">
                                                                                <li style="Margin:0 0 5px; list-style:disc inside; mso-special-format:bullet;">
                                                                                    Item one
                                                                                </li>
                                                                                <li style="Margin:0 0 5px; list-style:disc inside; mso-special-format:bullet;">
                                                                                    Item two
                                                                                </li>

                                                                            </ul>

                                                                            <ul>
                                                                            <#assign hasMacBinding>${hasMacBinding}</#assign>
                                                                            <#if hasMacBinding=='Y'>
                                                                                Mac Address(s): <br/>
                                                                                <#list updatedMacAddress as macAddress>
                                                                                ${macAddress_index + 1}
                                                                                    . ${macAddress}<#if macAddress_has_next>
                                                                                    <br/></#if>
                                                                                </#list>
                                                                            <#else>
                                                                                Mac Address(s):
                                                                                <#list updatedMacAddress as macAddress>
                                                                                ${macAddress}
                                                                                </#list>
                                                                            </#if>
                                                                            </ul>


                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                                <!-- // CONTENT TABLE -->

                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // FLEXIBLE CONTAINER -->
                                    </td>
                                </tr>
                            </table>
                            <!-- // CENTERING TABLE -->
                        </td>
                    </tr>
                    <!-- // MODULE ROW -->


                    <!-- MODULE ROW // -->
                    <tr>
                        <td align="center" valign="top">
                            <!-- CENTERING TABLE // -->
                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                <tr>
                                    <td align="center" valign="top">
                                        <!-- FLEXIBLE CONTAINER // -->
                                        <table border="0" cellpadding="30" cellspacing="0" width="500"
                                               class="flexibleContainer">
                                            <tr>
                                                <td valign="top" width="500" class="flexibleContainerCell">

                                                    <!-- CONTENT TABLE // -->
                                                    <table align="left" border="0" cellpadding="0" cellspacing="0"
                                                           width="100%">
                                                        <tr>
                                                            <td align="left" valign="top" class="flexibleContainerBox">
                                                                <table border="0" cellpadding="0" cellspacing="0"
                                                                       width="210" style="max-width: 100%;">
                                                                    <tr>
                                                                        <td align="left" class="textContent">

                                                                            <div style="text-align:left;font-family:Helvetica,Arial,sans-serif;font-size:15px;margin-bottom:0;color:#101010;line-height:135%;">
                                                                                Best Wishes,
                                                                            </div>
                                                                            <div style="text-align:left;font-family:Helvetica,Arial,sans-serif;font-size:15px;margin-bottom:0;color:#5F5F5F;line-height:135%;">
                                                                                Cogent Team
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </td>

                                                        </tr>
                                                    </table>
                                                    <!-- // CONTENT TABLE -->

                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // FLEXIBLE CONTAINER -->
                                    </td>
                                </tr>
                            </table>
                            <!-- // CENTERING TABLE -->
                        </td>
                    </tr>
                    <!-- // MODULE ROW -->


                    <!-- MODULE ROW // -->
                    <tr>
                        <td align="center" valign="top">
                            <!-- CENTERING TABLE // -->
                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                <tr>
                                    <td align="center" valign="top">
                                        <!-- FLEXIBLE CONTAINER // -->
                                        <table border="0" cellpadding="0" cellspacing="0" width="500"
                                               class="flexibleContainer">
                                            <tr>
                                                <td align="center" valign="top" width="500"
                                                    class="flexibleContainerCell">
                                                    <table border="0" cellpadding="30" cellspacing="0" width="100%">
                                                        <tr>
                                                            <td align="center" valign="top">

                                                                <!-- CONTENT TABLE // -->
                                                                <table border="0" cellpadding="0" cellspacing="0"
                                                                       width="50%" class="emailButton"
                                                                       style="background-color: #fff;">
                                                                    <tr>
                                                                        <td align="center" valign="middle"
                                                                            class="buttonContent"
                                                                            style="padding-top:15px;padding-bottom:15px;padding-right:15px;padding-left:15px;">
                                                                            <img src="cid:logo.png"
                                                                                 width="10" class="flexibleImage"
                                                                                 style="max-width:80px;width:100%;display:block;margin:0 auto;"
                                                                                 alt="Text" title="Text"/>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                                <!-- // CONTENT TABLE -->

                                                                <!-- CONTENT TABLE // -->
                                                                <table border="0" cellpadding="0" cellspacing="0"
                                                                       width="100%">
                                                                    <tr>
                                                                        <td align="center" valign="top"
                                                                            class="textContent">
                                                                            <div style="text-align:center;font-family:Helvetica,Arial,sans-serif;font-size:15px;margin-bottom:0;margin-top:3px;color:#5F5F5F;line-height:135%;">
                                                                                Cogent EMR
                                                                            </div>
                                                                            <div style="text-align:center;font-family:Helvetica,Arial,sans-serif;font-size:15px;margin-bottom:0;margin-top:3px;color:#959191;line-height:135%;">
                                                                                4th floor M&S Towers, Pulchowk Road,
                                                                                Patan, Nepal
                                                                            </div>
                                                                            <div style="text-align:center;font-family:Helvetica,Arial,sans-serif;font-size:15px;margin-bottom:0;margin-top:3px;color:#959191;line-height:135%;">
                                                                                Phone: 01-5525261
                                                                            </div>
                                                                            <div style="text-align:center;font-family:Helvetica,Arial,sans-serif;font-size:15px;margin-bottom:0;margin-top:3px;color:#959191;line-height:135%;">
                                                                                Email:&nbsp;<a
                                                                                    href="mailto:name@rapidtables.com"
                                                                                    style="border-radius:0;color:#959191">info@cogenthealth.com.np</a>
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                                <!-- // CONTENT TABLE -->

                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // FLEXIBLE CONTAINER -->
                                    </td>
                                </tr>
                            </table>
                            <!-- // CENTERING TABLE -->
                        </td>
                    </tr>
                    <!-- // MODULE ROW -->

                </table>
                <!-- // END -->

                <!-- EMAIL FOOTER // -->
                <!--
                    The table "emailBody" is the email's container.
                    Its width can be set to 100% for a color band
                    that spans the width of the page.
                -->
                <table bgcolor="#E1E1E1" border="0" cellpadding="0" cellspacing="0" width="500" id="emailFooter">

                    <!-- FOOTER ROW // -->
                    <!--
                        To move or duplicate any of the design patterns
                        in this email, simply move or copy the entire
                        MODULE ROW section for each content block.
                    -->
                    <tr>
                        <td align="center" valign="top">
                            <!-- CENTERING TABLE // -->
                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                <tr>
                                    <td align="center" valign="top">
                                        <!-- FLEXIBLE CONTAINER // -->
                                        <table border="0" cellpadding="0" cellspacing="0" width="500"
                                               class="flexibleContainer">
                                            <tr>
                                                <td align="center" valign="top" width="500"
                                                    class="flexibleContainerCell">
                                                    <table border="0" cellpadding="30" cellspacing="0" width="100%">
                                                        <tr>
                                                            <td valign="top" bgcolor="#E1E1E1">

                                                                <div style="font-family:Helvetica,Arial,sans-serif;font-size:13px;color:#828282;text-align:center;line-height:120%;">
                                                                    <div>Copyright &#169; 2020 <a
                                                                            href="http://www.cogenthealth.com.np/"
                                                                            target="_blank"
                                                                            style="text-decoration:none;color:#828282;border-radius:0px;"><span
                                                                            style="color:#828282;">Cogent EMR</span></a>.
                                                                        All&nbsp;rights&nbsp;reserved.
                                                                    </div>

                                                                </div>

                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        <!-- // FLEXIBLE CONTAINER -->
                                    </td>
                                </tr>
                            </table>
                            <!-- // CENTERING TABLE -->
                        </td>
                    </tr>

                </table>
                <!-- // END -->

            </td>
        </tr>
    </table>
</center>
</body>
</html>
