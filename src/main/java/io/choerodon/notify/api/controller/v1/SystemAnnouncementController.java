package io.choerodon.notify.api.controller.v1;

import java.util.Date;
import javax.validation.Valid;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import io.choerodon.base.annotation.Permission;
import io.choerodon.base.domain.PageRequest;
import io.choerodon.base.domain.Sort;
import io.choerodon.base.enums.ResourceType;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.iam.InitRoleCode;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.annotation.SortDefault;
import io.choerodon.notify.api.dto.SystemAnnouncementDTO;
import io.choerodon.notify.api.service.SystemAnnouncementService;
import io.choerodon.notify.api.vo.SystemNoticeSearchVO;
import io.choerodon.swagger.annotation.CustomPageRequest;

/**
 * @author dengyouquan, Eugen
 **/
@RestController
@RequestMapping(value = "/v1/system_notice")
public class SystemAnnouncementController {
    private SystemAnnouncementService systemAnnouncementService;

    public SystemAnnouncementController(SystemAnnouncementService systemAnnouncementService) {
        this.systemAnnouncementService = systemAnnouncementService;
    }

    public void setSystemAnnouncementService(SystemAnnouncementService systemAnnouncementService) {
        this.systemAnnouncementService = systemAnnouncementService;
    }

    @Permission(type = ResourceType.SITE, roles = {InitRoleCode.SITE_ADMINISTRATOR})
    @ApiOperation(value = "新增系统公告")
    @PostMapping("/create")
    public ResponseEntity<SystemAnnouncementDTO> create(@RequestBody @Valid SystemAnnouncementDTO dto) {
        dto.setStatus(SystemAnnouncementDTO.AnnouncementStatus.WAITING.value());
        if (dto.getSendDate().getTime() < System.currentTimeMillis()) {
            throw new CommonException("error.create.system.announcement.sendDate.cant.before.now");
        }
        if (dto.getSendDate() == null) {
            dto.setSendDate(new Date());
        }
        if (dto.getSticky() != null && dto.getSticky() && dto.getEndDate() == null) {
            throw new CommonException("error.create.system.announcement.endDate.is.null");
        }
        return new ResponseEntity<>(systemAnnouncementService.create(dto), HttpStatus.OK);
    }

    @Permission(type = ResourceType.SITE, roles = {InitRoleCode.SITE_ADMINISTRATOR})
    @ApiOperation(value = "分页查询系统公告")
    @PostMapping("/all/list")
    @CustomPageRequest
    public ResponseEntity<PageInfo<SystemAnnouncementDTO>> pagingQuery(@ApiIgnore
                                                                       @SortDefault(value = "id", direction = Sort.Direction.DESC) PageRequest pageRequest,
                                                                       @RequestBody SystemNoticeSearchVO systemNoticeSearchVO) {
        return new ResponseEntity<>(systemAnnouncementService.pagingQuery(pageRequest, systemNoticeSearchVO), HttpStatus.OK);
    }

    @Permission(type = ResourceType.SITE, permissionLogin = true)
    @ApiOperation(value = "分页查询已发送的系统公告")
    @CustomPageRequest
    @GetMapping("/completed")
    public ResponseEntity<PageInfo<SystemAnnouncementDTO>> pagingQueryCompleted(@ApiIgnore
                                                                                @SortDefault(value = "id", direction = Sort.Direction.DESC) PageRequest pageRequest) {
        SystemNoticeSearchVO systemNoticeSearchVO = new SystemNoticeSearchVO();
        systemNoticeSearchVO.setStatus(SystemAnnouncementDTO.AnnouncementStatus.COMPLETED.value());
        return new ResponseEntity<>(systemAnnouncementService.pagingQuery(pageRequest, systemNoticeSearchVO), HttpStatus.OK);
    }


    @Permission(type = ResourceType.SITE, roles = {InitRoleCode.SITE_ADMINISTRATOR})
    @ApiOperation(value = "查看系统公告详情")
    @GetMapping("/{id}")
    public ResponseEntity<SystemAnnouncementDTO> getDetail(@PathVariable("id") Long id) {
        return new ResponseEntity<>(systemAnnouncementService.getDetailById(id), HttpStatus.OK);
    }


    @Permission(type = ResourceType.SITE, roles = {InitRoleCode.SITE_ADMINISTRATOR})
    @ApiOperation(value = "删除系统公告")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        systemAnnouncementService.delete(id);
    }

    @Permission(type = ResourceType.SITE, roles = {InitRoleCode.SITE_ADMINISTRATOR})
    @ApiOperation(value = "更新系统公告")
    @PutMapping("/update")
    public ResponseEntity<SystemAnnouncementDTO> update(@RequestBody @Validated SystemAnnouncementDTO dto) {
        return new ResponseEntity<>(systemAnnouncementService.update(dto, ResourceLevel.SITE, 0L), HttpStatus.OK);
    }

    @Permission(type = ResourceType.SITE, permissionLogin = true)
    @ApiOperation(value = "查询当前需悬浮显示的最新公告")
    @GetMapping("/new_sticky")
    public ResponseEntity<SystemAnnouncementDTO> getNewSticky() {
        return new ResponseEntity<>(systemAnnouncementService.getLatestSticky(), HttpStatus.OK);
    }
}
