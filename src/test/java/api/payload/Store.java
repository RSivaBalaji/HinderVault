package api.payload;

public class Store 
{
	int id;
	int petId;
	int quantity;
	String shipDate;
	String status;
	boolean complete;
	
	public int getId() 
	{
		return id;
	}
	public void setId(int id) 
	{
		this.id = id;
	}
	public int getPetid() 
	{
		return petId;
	}
	public void setPetid(int petid) 
	{
		
		this.petId = 23;
	}
	public int getQuantity() 
	{
		return quantity;
	}
	public void setQuantity(int quantity) 
	{
		this.quantity = quantity;
	}
	public String getDate() 
	{
		return shipDate;
	}
	public void setDate(String date)
	{
		this.shipDate = date;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public boolean isComplete()
	{
		return complete;
	}
	public void setComplete(boolean complete)
	{
		this.complete = complete;
	}	
}
