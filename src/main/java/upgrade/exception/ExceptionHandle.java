package upgrade.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * My responsibility is handle meaningfull exceptions.
 * @author giuseppe
 */
@ControllerAdvice
public class ExceptionHandle {
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaximumThirtyDaysInAdvance.class)
    @ResponseBody ErrorInfo 
    handleMaximumThirtyDaysInAdvance(HttpServletRequest req, Exception ex) {
        return new ErrorInfo( "the maximum days for reservation in advance is 30.");
    }
	
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MinimumOneDayAheadArrival.class)
    @ResponseBody ErrorInfo 
    handleMinimumOneDayAheadArrival(HttpServletRequest req, Exception ex) {
        return new ErrorInfo( "the minimum days ahead of arrival for reservation is one.");
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody ErrorInfo 
    handleDataIntegrityViolationException(HttpServletRequest req, Exception ex) {
        return new ErrorInfo( "the períod you are asking for is not available anymore, please choose another one.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaximumBookingDays.class)
    @ResponseBody ErrorInfo 
    handleMaximumBookingDays(HttpServletRequest req, Exception ex) {
        return new ErrorInfo( "the maximum períod accepted is tree days.");
    }

}
