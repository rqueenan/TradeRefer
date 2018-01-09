<%@ page contentType="text/html" %>
<img src="${grailsApplication.config.serverEndpoint}/newRegistrations/logo/${invoiceNumber}" />
<p>Dear <b>${name}</b>,</p>
<p>You’ve recently had a job completed by ${tradesmanCompanyName} and they have recorded this on TradeRefer, a website that allows you to pay for work at your convenience.  If you register at the link below, with very little information, you’ll be able to pay the outstanding invoice as well as contacting any other tradesmen who are doing work for you.</p>
<p><a href="${grailsApplication.config.serverEndpoint}/newRegistrations/createNewCustomer?contactName=${name}&contactEmail=${customerEmail}&invoiceID=${invoiceNumber}">Registration Link</a></p>
<p>Once you have opened the invoice and you’re happy with the amount contained within, you are able to pay electronically using either PayPal or Amazon Pay - none of your account details will be stored by us at TradeRefer.</p>
<p>If you would rather pay this in cash then please contact <b>${tradesmanCompanyName}</b> directly to arrange payment.</p>

<br>
<p>Yours Sincerely,<br>TradeRefer Team</p>