

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'traderapp.user.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'traderapp.user.UserRole'
grails.plugin.springsecurity.authority.className = 'traderapp.user.Role'
grails.plugin.springsecurity.logout.postOnly = false

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/home/**',        access: ['permitAll']],
	[pattern: '/*',              access: ['ROLE_ADMIN', 'ROLE_CUSTOMER', 'ROLE_TRADESMAN']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']],
	[pattern: '/testDomain/*',   access: ['permitAll']],
	[pattern: '/newRegistrations/**', access: ['permitAll']],
	[pattern: '/admin/**', access: ['ROLE_ADMIN']],
	[pattern: '/tradesman/**', access:['ROLE_TRADESMAN']],
	[pattern: '/customer/**', access:['ROLE_CUSTOMER']],
	[pattern: '/facebookUser/**', access:['ROLE_FACEBOOK_CUSTOMER']],
	[pattern: '/linkedInUser/**', access:['ROLE_LINKEDIN_CUSTOMER']],
	[pattern: '/profile/**',      access:['ROLE_CUSTOMER', 'ROLE_FACEBOOK_CUSTOMER', 'ROLE_LINKEDIN_CUSTOMER']],
	[pattern: '/j_spring_security_facebook_redirect', access:['permitAll']],
	[pattern: '/search/**',              access: ['ROLE_ADMIN', 'ROLE_CUSTOMER', 'ROLE_TRADESMAN', 'ROLE_FACEBOOK_CUSTOMER', 'ROLE_LINKEDIN_CUSTOMER']],
	[pattern: '/jobs/**',                access: ['ROLE_ADMIN', 'ROLE_CUSTOMER', 'ROLE_TRADESMAN', 'ROLE_FACEBOOK_CUSTOMER', 'ROLE_LINKEDIN_CUSTOMER']],
	[pattern: '/invoice/**',                access: ['ROLE_ADMIN', 'ROLE_CUSTOMER', 'ROLE_TRADESMAN']],
	[pattern: '/emailTemplates/**', access:['permitAll']],
	[pattern: '/paypal/**', access:['ROLE_CUSTOMER', 'ROLE_FACEBOOK_CUSTOMER', 'ROLE_LINKEDIN_CUSTOMER']],
	[pattern: '/amazonPay/**', access:['ROLE_CUSTOMER', 'ROLE_FACEBOOK_CUSTOMER', 'ROLE_LINKEDIN_CUSTOMER']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]

environments{
	development{
		paypal.endpoint = "https://api.sandbox.paypal.com"
		amazonPay.redirectUrl = "http://localhost:8080/amazonPay/pay?invoice="
		paypal.returnUrl = "http://localhost:8080/paypal/execute?invoiceID="
		paypal.cancelUrl = "http://localhost:8080/paypal/cancel?invoiceID="
		serverEndpoint = "http://localhost:8080"
	}
	test{
		paypal.endpoint = "https://api.sandbox.paypal.com"
		amazonPay.redirectUrl = "https://traderefer.co.uk/amazonPay/pay?invoice="
		paypal.returnUrl = "https://traderefer.co.uk/paypal/execute?invoiceID="
		paypal.cancelUrl = "https://traderefer.co.uk/paypal/cancel?invoiceID="
		serverEndpoint = "https://traderefer.co.uk"
	}
	production{
		paypal.endpoint = "https://api.paypal.com"
	}
}