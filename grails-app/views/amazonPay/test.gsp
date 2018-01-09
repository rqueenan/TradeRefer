		<meta name="layout" content="customer_main" />
		<script>
  			window.onAmazonLoginReady = function() { 
    			amazon.Login.setClientId('amzn1.application-oa2-client.3f6b64447b764f6fb3e438e0d154d78a'); 
  			};
  			window.onAmazonPaymentsReady = function(){
  			    // render the button here
  			    var authRequest; 

  			    OffAmazonPayments.Button('LoginWithAmazon', 'A36CCH24SKY06R', {
  			      type:  "PwA", 
  			      color: "Gold", 
  			      size:  "small", 
  			      language: "en-GB",

  			      authorization: function() { 
  			        loginOptions = {scope: "profile payments:widget", popup: "false"}; 
  			        authRequest = amazon.Login.authorize (loginOptions, "http://localhost:8080/amazonPay/pay"); 
  			      },
  			    onError: function(error) { 
  			      // your error handling code.
  			       alert("The following error occurred: " 
  			         + error.getErrorCode() 
  			         + ' - ' + error.getErrorMessage());
  			    }  
  			    });
  			  }
		</script>
		<script async="async" type='text/javascript' 
    src='https://static-eu.payments-amazon.com/OffAmazonPayments/gbp/sandbox/lpa/js/Widgets.js'>
  </script>
		 <div id="LoginWithAmazon"></div>	
