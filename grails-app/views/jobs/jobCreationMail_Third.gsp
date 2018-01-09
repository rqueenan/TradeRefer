<%@ page contentType="text/html" %>
<img src="${grailsApplication.config.serverEndpoint}/newRegistrations/tradesman_logo/${jobId}" />
<p>Dear <b>${companyName}</b>,</p>
<p>By registering with TradeRefer you’ll be able to reduce the amount of time you spend doing admin, expand your customer base using the power of social media and have existing customers search for you if they need work done.</p>
<p>All you need to do is enter some basic information using the link below and then you’ll be able to start adding jobs!</p>
<br>
<p><a href="${grailsApplication.config.serverEndpoint}/newRegistrations/createNewTradesman?companyName=${companyName}&companyEmail=${companyEmail}&jobId=${jobId}">Registration Link</a></p>
<br>
<p>Kind regards,<br>TradeRefer Team</p>