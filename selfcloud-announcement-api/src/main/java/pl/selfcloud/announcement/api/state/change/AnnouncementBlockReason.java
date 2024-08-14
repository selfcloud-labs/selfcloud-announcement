package pl.selfcloud.announcement.api.state.change;

public enum AnnouncementBlockReason {
  SCAM, WRONG_MARKED_CONTENT, FAILURE_TO_FULFILL_CONTRACT, OTHER;

  private String details;
}
