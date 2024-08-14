package pl.selfcloud.announcement.api.state.change;

public enum AnnouncementCancelReason {
  ISSUE_TIME_PASSED, SELLER_DECISION;

  private String details;
}
