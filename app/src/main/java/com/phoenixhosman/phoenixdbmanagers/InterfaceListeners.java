package com.phoenixhosman.phoenixdbmanagers;

/**
 * The interface listeners class
 *
 * This class contain the interface listeners needed by the app
 *
 * @author Troy Marker
 * @version 1.0.0
 */
public class InterfaceListeners {

    /**
     * Interface listener for the grade response
     */
    public interface onGradeResponseListener {
        void onGradeResponse(String grade);
    }

    /**
     * Interface listener for the department response
     */
    public interface onDepartmentResponseListener {
        void onDepartmentResponse(String department);
    }
}
