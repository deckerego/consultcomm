public class ProjectMap implements java.io.Serializable {
    private String alias = "";
    private boolean export;
    
    public ProjectMap() {
        this.alias = "";
        this.export = false;
    }
    
    public ProjectMap(String alias, boolean export) {
        this.alias = alias;
        this.export = export;
    }
    
    public void setAlias(String alias) { this.alias = alias; }
    public String getAlias() { return this.alias; }
    public void setExport(boolean export) { this.export = export; }
    public boolean getExport() { return this.export; }
    public boolean isExport() { return this.export; }
}
