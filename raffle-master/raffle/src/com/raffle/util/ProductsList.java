package com.raffle.util;

import java.util.List;

import com.raffle.pojo.Product;

public class ProductsList {

	private Product carProduct;
	private Product vacProduct;
	private Product bundleProduct;

	public Product getCarProduct() {
		return carProduct;
	}

	public void setCarProduct(Product carProduct) {
		this.carProduct = carProduct;
	}

	public Product getVacProduct() {
		return vacProduct;
	}

	public void setVacProduct(Product vacProduct) {
		this.vacProduct = vacProduct;
	}

	public Product getBundleProduct() {
		return bundleProduct;
	}

	public void setBundleProduct(Product bundleProduct) {
		this.bundleProduct = bundleProduct;
	}

	public ProductsList(List<Product> allProducts)
	{
		for(Product product : allProducts)
		{
			if(product.getIdProduct() == 1)
			{
				carProduct = product;
				continue;
			}
			
			if(product.getIdProduct() == 2)
			{
				vacProduct = product;
				continue;
			}
			if(product.getIdProduct() == 3)
			{
				bundleProduct = product;
				continue;
			}
		}
	}
}
