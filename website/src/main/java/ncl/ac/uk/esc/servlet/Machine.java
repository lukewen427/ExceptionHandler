package ncl.ac.uk.esc.servlet;

public class Machine {
	private String ip;
	private String statue;
	private String security;
	
	public Machine(String ip,String statue,String security){
		setIp(ip);
		setStatue(statue);
		setSecurity(security);
	}
	public String getIp(){
		return ip;
	}
	public void setIp(String ip){
		this.ip=ip;
	}
	
	public String getStatue(){
		return statue;
	}
	public void setStatue(String statue){
		this.statue=statue;
	}
	
	public String getSecurity(){
		return security;
	}
	public void setSecurity(String security){
		this.security=security;
	}
	
	@Override
	public String toString(){
		return "Machine [ip=" + ip + ", Staute=" + statue + ", security="
                + security + "]";
		}
}
