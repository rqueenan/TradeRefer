<%@ page contentType="text/html" %>
<img src="${grailsApplication.config.serverEndpoint}/newRegistrations/tradesman_logo/${jobId}" />
<p>Dear <b>${companyName}</b>,</p>
<p>${customerName}&nbsp; has registered with us at TradeRefer and would like to use the site to process the job. They will have already entered the basics job details so all you need to do is register, complete the job details then email the invoice to them - this will allow them to pay you electronically too if you want!</p>
<p>Click on the link below, register with a few basic details, then you'll see the job on your home page.</p>
<br>
<p><a href="${grailsApplication.config.serverEndpoint}/newRegistrations/createNewTradesman?companyName=${companyName}&companyEmail=${companyEmail}&jobId=${jobId}">Registration Link</a></p>
<br>
<p>Kind regards,<br>TradeRefer Team</p>