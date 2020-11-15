package pl.wiktor.multiplication;

import pl.wiktor.multiplication.service.SessionService;

//IT ALLOWS ME TO HAVE A ONLY ONE REFERENCE IN A WHOLE PROJECT SO I DON'T NEED TO CARE ABOUT
//ANOTHER REFERENCE IN ANOTHER CLASS BY ACCIDENTAL CREATING NEW OBJECT FROM SESSION_SERVICE
//ITS A SINGLETON DESIGN PATTERN
public enum Singleton {
    SESSION_SERVICE;

    private SessionService sessionService = new SessionService();

    public SessionService getSessionService(){
        return sessionService;
    }
}
