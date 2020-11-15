package pl.wiktor.multiplication;

import pl.wiktor.multiplication.service.SessionService;

public enum Singleton {
    SESSION_SERVICE;

    private SessionService sessionService = new SessionService();

    public SessionService getSessionService(){
        return sessionService;
    }
}
