package ashad.app.torch.flashlight.noticeaccepter;

public class nticegetter {
    private String title;
    private String dateandtime;
    private String imageurl;
    private String notice;
    private String approvedornot;
    private String UniqueId;

    public String getUniqueId() {
        return UniqueId;
    }

    public String getApprovedornot() {
        return approvedornot;
    }

    public String getSendedby() {
        return sendedby;
    }

    private String sendedby;

    public nticegetter() {
    }

    public String getTitle() {
        return title;
    }

    public String getDateandtime() {
        return dateandtime;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getNotice() {
        return notice;
    }
}
