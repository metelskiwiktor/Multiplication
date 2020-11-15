package pl.wiktor.multiplication.repository;

import java.util.ArrayList;
import java.util.List;

import pl.wiktor.multiplication.model.Session;

//A CLASS THAT IMITATE A DATABASE, I DONT' WRITE DATA TO FILE/DATABASE BUT TO VARIABLES. ONLY READ AND SAVE METHODS
public class SessionRepository {
    private List<Session> sessions = new ArrayList<>();

    public void save(Session session) {
        sessions.add(session);
    }

    public List<Session> getSessions() {
        return sessions;
    }
}
