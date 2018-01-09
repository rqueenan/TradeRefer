<%@ page contentType="text/html" %>

<img src="${grailsApplication.config.serverEndpoint}/newRegistrations/logo/${invoiceNumber}" />
<p>Dear <b>${customerName}</b>,</p>
<p>It’s been 7 days since you were sent an invoice from <b>${companyName}</b> regarding the job they recently completed for you. </p>
<p>They have recorded this on TradeRefer, a website that allows you to pay for work at your convenience.  If you register at the link below, with very little information, you’ll be able to pay the outstanding invoice as well as contacting any other tradesmen who are doing work for you.</p>
<p><a href="${grailsApplication.config.serverEndpoint}/newRegistrations/createNewCustomer?contactName=${customerName}&contactEmail=${customeEmail}&invoiceID=${invoiceNumber}">Registration Link</a></p>
<p>Once you have opened the invoice and you’re happy with the amount contained within, you are able to pay electronically using either PayPal or Amazon Pay - none of your account details will be stored by us at TradeRefer.</p>
<p>If you would rather pay this in cash then please contact <b>${companyName}</b> directly to arrange payment.</p>
<br>
<p>Kind regards,<br>TradeRefer Team</p>