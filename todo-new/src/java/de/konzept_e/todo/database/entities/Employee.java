/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.konzept_e.todo.database.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.Session;

/**
 *
 * @author sven
 */
@Entity
@Table(name = "employee")
public class Employee implements Serializable
{
	@Id
	@Column(name = "employeeId", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "firstname")
	private String firstname;

	@Column(name = "lastname")
	private String lastname;

	@Column(name = "address")
	private String address;

	@Column(name = "cityCode")
	private String cityCode;

	@Column(name = "city")
	private String city;

	@Column(name = "telephone")
	private String telephone;

	@Column(name = "cellphone")
	private String cellphone;

	@Column(name = "businessEmail")
	private String businessEmail;

	@Column(name = "privateEmail")
	private String privateEmail;
	
	@Column(name = "gender")
	private String gender;

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname()
	{
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname()
	{
		return lastname;
	}

	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}

	/**
	 * @return the address
	 */
	public String getAddress()
	{
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address)
	{
		this.address = address;
	}

	/**
	 * @return the cityCode
	 */
	public String getCityCode()
	{
		return cityCode;
	}

	/**
	 * @param cityCode the cityCode to set
	 */
	public void setCityCode(String cityCode)
	{
		this.cityCode = cityCode;
	}

	/**
	 * @return the city
	 */
	public String getCity()
	{
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city)
	{
		this.city = city;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone()
	{
		return telephone;
	}

	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
	}

	/**
	 * @return the cellphone
	 */
	public String getCellphone()
	{
		return cellphone;
	}

	/**
	 * @param cellphone the cellphone to set
	 */
	public void setCellphone(String cellphone)
	{
		this.cellphone = cellphone;
	}

	/**
	 * @return the businessEmail
	 */
	public String getBusinessEmail()
	{
		return businessEmail;
	}

	/**
	 * @param businessEmail the businessEmail to set
	 */
	public void setBusinessEmail(String businessEmail)
	{
		this.businessEmail = businessEmail;
	}

	/**
	 * @return the privateEmail
	 */
	public String getPrivateEmail()
	{
		return privateEmail;
	}

	/**
	 * @param privateEmail the privateEmail to set
	 */
	public void setPrivateEmail(String privateEmail)
	{
		this.privateEmail = privateEmail;
	}

	/**
	 * @return the gender
	 */
	public String getGender()
	{
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public static Employee getDefaultElement(Session dbSession)
	{
		@SuppressWarnings("unchecked")
		List<Employee> employees = dbSession.createQuery("FROM Employee ORDER BY lastname ASC, firstname ASC").list();
		return employees.get(0);
	}

	public String toString()
	{
		return lastname + ", " + firstname;
	}
}
