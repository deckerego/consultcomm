public class ProjectInvalidException extends Exception {
  String message = "ProjectInvalidException";

  public ProjectInvalidException() {
  }

  public ProjectInvalidException(String msg) {
    message += ": "+msg;
  }

  public String toString() {
    return message;
  }

  public String getMessage() {
    return message;
  }
}
