package netease.li.com.wangyiyun.news.bean;

public class SpecialItemBean {

    /**
     * digest :
     * docid : C3OE5CUS00238087
     * imgsrc : http://cms-bucket.nosdn.127.net/f7dffdbf71f943909bc6f7694d89316520161019142434.jpeg
     * ipadcomment :
     * lmodify : 2016-10-19 14:26:05
     * ltitle : 习近平与他的作家“朋友圈”
     * postid : C3OE5CUS00238087
     * ptime : 2016-10-19 14:24:09
     * replyCount : 0
     * source : 文汇网
     * tag :
     * title : 习近平与他的作家“朋友圈”
     * url : http://3g.163.com/ntes/16/1019/14/C3OE5CUS00238087.html
     * votecount : 0
     */

    private String digest;
    private String docid;
    //图片
    private String imgsrc;
    private String ipadcomment;
    private String lmodify;
    //标题
    private String ltitle;
    private String postid;
    private String ptime;
    private int replyCount;
    private String source;
    private String tag;
    private String title;
    private String url;
    //评论数
    private int votecount;



    //标题栏有用
    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    //这三个参数对标题栏有用
    private boolean isTitle = false;
    private String title_name;
    private String  index;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getIpadcomment() {
        return ipadcomment;
    }

    public void setIpadcomment(String ipadcomment) {
        this.ipadcomment = ipadcomment;
    }

    public String getLmodify() {
        return lmodify;
    }

    public void setLmodify(String lmodify) {
        this.lmodify = lmodify;
    }

    public String getLtitle() {
        return ltitle;
    }

    public void setLtitle(String ltitle) {
        this.ltitle = ltitle;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVotecount() {
        return votecount;
    }

    public void setVotecount(int votecount) {
        this.votecount = votecount;
    }

    @Override
    public String toString() {
        return "SpecialItemBean{" +
                "digest='" + digest + '\'' +
                ", docid='" + docid + '\'' +
                ", imgsrc='" + imgsrc + '\'' +
                ", ipadcomment='" + ipadcomment + '\'' +
                ", lmodify='" + lmodify + '\'' +
                ", ltitle='" + ltitle + '\'' +
                ", postid='" + postid + '\'' +
                ", ptime='" + ptime + '\'' +
                ", replyCount=" + replyCount +
                ", source='" + source + '\'' +
                ", tag='" + tag + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", votecount=" + votecount +
                ", isTitle=" + isTitle +
                ", title_name='" + title_name + '\'' +
                ", index='" + index + '\'' +
                '}';
    }
}