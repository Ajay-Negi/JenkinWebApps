package com.dmi.dao.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author MBansal
 *
 */

@Entity
@Table(name = "ml_models_types")
@NamedQuery(name = "MLModelsAndExp.findAll", query = "SELECT c FROM MLModelsAndExp c")
public class MLModelsTypes implements Serializable
{
	private static final long serialVersionUID = 1L;

	public MLModelsTypes()
	{

	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long modelTypeId;

	@Column(name = "MODEL_TYPE")
	private String modelType;

	// bi-directional many-to-one association to MLModelsAndExp
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "mlModelsTypes")
	private List<MLModelsAndExp> mlModelsAndExp;

	public Long getModelTypeId()
	{
		return modelTypeId;
	}

	public void setModelTypeId(Long modelTypeId)
	{
		this.modelTypeId = modelTypeId;
	}

	public String getModelType()
	{
		return modelType;
	}

	public void setModelType(String modelType)
	{
		this.modelType = modelType;
	}

	public List<MLModelsAndExp> getMlModelsAndExp()
	{
		return mlModelsAndExp;
	}

	public void setMlModelsAndExp(List<MLModelsAndExp> mlModelsAndExp)
	{
		this.mlModelsAndExp = mlModelsAndExp;
	}

	public MLModelsAndExp addMLModelsAndExp(MLModelsAndExp mlModelsAndExp)
	{
		getMlModelsAndExp().add(mlModelsAndExp);
		mlModelsAndExp.setMlModelsTypes(this);

		return mlModelsAndExp;
	}

	public MLModelsAndExp removeMLModelsAndExp(MLModelsAndExp mlModelsAndExp)
	{
		getMlModelsAndExp().remove(mlModelsAndExp);
		mlModelsAndExp.setMlModelsTypes(null);

		return mlModelsAndExp;
	}
}
