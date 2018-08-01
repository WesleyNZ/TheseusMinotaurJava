package nz.ac.wew248.simplethesmin.model;

public interface ISubject {
    void attach(IObserver observer);
    void detach(IObserver observer);
    void inform();
}
