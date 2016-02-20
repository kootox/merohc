package org.chorem.merohc.services.v1;

import org.chorem.merohc.bean.ProjectDTO;
import org.chorem.merohc.entities.Project;
import org.chorem.merohc.entities.ProjectTopiaDao;
import org.nuiton.topia.persistence.TopiaNoResultException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Controller
@Transactional
public class ProjectController extends AbstractService {

    @ResponseBody
    @RequestMapping(value="/v1/project", method= RequestMethod.GET)
    public List<ProjectDTO> listProjects() {
        List<Project> projects = getProjectDao().findAll();

        List<ProjectDTO> dtos = new ArrayList<>();

        for (Project project:projects) {
            ProjectDTO dto = new ProjectDTO(project);
            dtos.add(dto);
        }

        return dtos;
    }

    @ResponseBody
    @RequestMapping(value="/v1/project", method= RequestMethod.PUT)
    public ProjectDTO addProject(@RequestBody ProjectDTO projectDTO) {
        Project projectToStore = getProjectDao().create();
        projectToStore.setName(projectDTO.getName());
        ProjectDTO dto = new ProjectDTO(projectToStore);
        return dto;
    }

    @ResponseBody
    @RequestMapping(value="/v1/project/{id:.+}", method= RequestMethod.GET)
    public ProjectDTO getProject(@PathVariable String id) {
        try {
            Project project = getProjectDao().forTopiaIdEquals(id).findAny();
            ProjectDTO dto = new ProjectDTO(project);
            return dto;
        } catch (TopiaNoResultException tnre) {
            return null;
        }
    }

    @ResponseBody
    @RequestMapping(value="/v1/project/{id:.+}", method= RequestMethod.DELETE)
    public void deleteProject(@PathVariable String id) {
        try {
            ProjectTopiaDao projectDao = getProjectDao();
            Project project = projectDao.forTopiaIdEquals(id).findAny();
            projectDao.delete(project);
        } catch (TopiaNoResultException tnre) {
            //Entity does not already exist, so nothing to do
        }
    }

    @ResponseBody
    @RequestMapping(value="/v1/project", method= RequestMethod.POST)
    public ProjectDTO editProject(@RequestBody ProjectDTO projectDTO) {

        Project project = getProjectDao().forTopiaIdEquals(projectDTO.getId()).findAny();
        //FIXME JC151211 - Deal with TopiaNoResultException
        project.setName(projectDTO.getName());
        ProjectDTO dto = new ProjectDTO(project);
        return dto;
    }

    protected ProjectTopiaDao getProjectDao() {
        return getPersistenceContext().getProjectDao();
    }

}
