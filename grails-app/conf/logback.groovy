import grails.util.BuildSettings

import java.nio.charset.Charset

import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')
        pattern = '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} ' + // Date
                        '%clr(%5p) ' + // Log level
                        '%clr(---){faint} %clr([%15.15t]){faint} ' + // Thread
                        '%clr(%-40.40logger{39}){cyan} %clr(:){faint} ' + // Logger
                        '%m%n%wex' // Message
    }
}

def targetDir = BuildSettings.TARGET_DIR
/*if (Environment.isDevelopmentMode() && targetDir != null) {
    
}*/

appender("RollingFile-Appender", RollingFileAppender) {
	file = "logs/TradeRefer.log"
	rollingPolicy(TimeBasedRollingPolicy) {
		fileNamePattern = "logs/TradeRefer.log%d{yyyy-MM-dd}.log"
	}
	encoder(PatternLayoutEncoder) {
		pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} --- [%level] --- [%logger] | %msg%n" // Message
	}
}
root(INFO, ["RollingFile-Appender","STDOUT"])

