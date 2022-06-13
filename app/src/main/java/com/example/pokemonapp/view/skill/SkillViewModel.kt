package com.example.pokemonapp.view.skill

import androidx.lifecycle.*
import com.example.pokemonapp.data.dao.SkillDAO
import com.example.pokemonapp.data.models.Skill
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class SkillViewModel(private val skillDAO: SkillDAO) : ViewModel() {

    val allSkills: LiveData<List<Skill>> = skillDAO.getAllSkills().asLiveData()

    fun getSkill(id: Int) : Skill {
        val allSkillsList = allSkills.value ?: listOf()

        return allSkillsList.find { currentSkill : Skill ->
            currentSkill.skill_id == id
        } ?: Skill(-1,"", "")
    }

    private fun insert(skill: Skill) {
        viewModelScope.launch {
            skillDAO.insert(skill = skill)
        }
    }

    private fun getNewSkillEntry(name: String, type: String, pp: Int) : Skill {
        return Skill(name = name, type = type, pp = pp)
    }

    fun createSkill(name: String, type: String, pp: Int) {
        insert(getNewSkillEntry(name, type, pp))
    }

    fun updateSkill(skill: Skill) {
        viewModelScope.launch {
            skillDAO.update(skill = skill)
        }
    }

    fun deleteSkill(skill: Skill) {
        viewModelScope.launch {
            skillDAO.delete(skill = skill)
        }
    }
}

class SkillViewModelFactory(private val skillDAO: SkillDAO) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SkillViewModel::class.java)) {
            return SkillViewModel(skillDAO = skillDAO) as T
        }

        throw IllegalArgumentException("Unknown view model class")
    }
}