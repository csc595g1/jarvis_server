package main.models;


public class RewardCatalog {
	
	private String brand;
	private String image_url;
	private String type;
	private String description;
	private String sku;
	private Boolean is_variable;
	private int denomination;
	private int min_price;
	private int max_price;
	private String currency_code;
	private Boolean available;
	private String country_code;
	
	public RewardCatalog() {}
	
	//For those records only having denomination amount and not min/max price
	public RewardCatalog(String brand, String image_url, String type, String description, String sku, Boolean is_variable, int denomination, String currency_code, Boolean available, String country_code) {
		
		this.brand = brand;
		this.image_url = image_url;
		this.type = type;
		this.description = description;
		this.sku = sku;
		this.is_variable = is_variable;
		this.denomination = denomination;
		this.currency_code = currency_code;
		this.available = available;
		this.country_code = country_code;
		
	}

	//For those records only having min/max price and not denomination amount
	public RewardCatalog(String brand, String image_url, String type, String description, String sku, Boolean is_variable, int min_price, int max_price, String currency_code, Boolean available, String country_code) {
		
		this.brand = brand;
		this.image_url = image_url;
		this.type = type;
		this.description = description;
		this.sku = sku;
		this.is_variable = is_variable;
		this.min_price = min_price;
		this.max_price = max_price;
		this.currency_code = currency_code;
		this.available = available;
		this.country_code = country_code;
		
	}
	
	
	
	
	
	
	
	
	
	
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Boolean getIs_variable() {
		return is_variable;
	}
	public void setIs_variable(Boolean is_variable) {
		this.is_variable = is_variable;
	}
	public int getDenomination() {
		return denomination;
	}
	public void setDenomination(int denomination) {
		this.denomination = denomination;
	}
	public int getMin_price() {
		return min_price;
	}
	public void setMin_price(int min_price) {
		this.min_price = min_price;
	}
	public int getMax_price() {
		return max_price;
	}
	public void setMax_price(int max_price) {
		this.max_price = max_price;
	}
	public String getCurrency_code() {
		return currency_code;
	}
	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}
	public Boolean getAvailable() {
		return available;
	}
	public void setAvailable(Boolean available) {
		this.available = available;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}


}
