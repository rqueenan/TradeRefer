<%@ page contentType="text/html" %>
<img src="${grailsApplication.config.serverEndpoint}/newRegistrations/logo/${invoiceNumber}" />
<p>Dear <b>${name}</b>,</p>
<p>Please find below a link to the invoice for the job you’ve recently had completed by <b>${tradesmanCompanyName}</b>.</p>
<p><a href="${grailsApplication.config.serverEndpoint}/profile/jobs">Invoice Link</a></p>
<p>Once you have opened the invoice and you’re happy with the amount contained within, you are able to pay electronically using either PayPal or Amazon Pay - none of your account details will be stored by us at TradeRefer.</p>
<p>If you would rather pay this in cash then please contact <b>${tradesmanCompanyName}</b> directly to arrange payment.</p>

<br>
<p>Yours Sincerely,<br>TradeRefer Team</p>