package business;




public class Login {

    private Integer custId;
    private String password;
    
    
    public Login(Integer custId, String password){
        this.custId= custId;
        this.password = password;
        
    }
    
    public Integer getCustId(){
        return custId;
    }
    public String getPassword() {
        return password;
    }
    
    
}
