// Train.java returns a Train object and each of its fields and the get methods. 

public class Train {
    private String carID;
    private String contents;
    private String originCity;
    private String destCity;
    private int weight;
    private int milesTraveled;

    public Train(String carID, String contents, String originCity, String destCity, int weight, int milesTraveled) {
        this.carID = carID;
        this.contents = contents;
        this.originCity = originCity;
        this.destCity = destCity;
        this.weight = weight;
        this.milesTraveled = milesTraveled;
    }

    public String getCarID() { 
		return carID; 
	}
    public String getContents() { 
		return contents; 
	}
    public String getOriginCity() { 
		return originCity; 
	}
    public String getDestCity() { 
		return destCity; 
	}
    public int getWeight() { 
		return weight; 
	}
    public int getMilesTraveled() { 
		return milesTraveled; 
	}

    // needed for the inspection rule
    public void setMilesTraveled(int milesTraveled) {
        this.milesTraveled = milesTraveled;
    }

    
    public String toString() {
        return carID + " containing " + contents;
    }
}// end class
