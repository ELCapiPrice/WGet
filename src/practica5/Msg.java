package practica5;

class Msg {
    private Boolean flag=false;
    private String content;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "flag=" + flag +
                ", content='" + content + '\'' +
                '}';
    }
}