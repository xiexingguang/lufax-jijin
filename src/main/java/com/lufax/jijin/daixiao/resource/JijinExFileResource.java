package com.lufax.jijin.daixiao.resource;

import com.lufax.jijin.base.utils.DateUtils;
import com.lufax.jijin.base.utils.Logger;
import com.lufax.jijin.daixiao.constant.DaiXiaoFileTypeEnum;
import com.lufax.jijin.daixiao.constant.JijinExAddFileResultEnum;
import com.lufax.jijin.daixiao.schedular.Jobs.ScanJijinExFileJob;
import com.lufax.jijin.fundation.constant.SyncFileStatus;
import com.lufax.jijin.fundation.dto.JijinSyncFileDTO;
import com.lufax.jijin.fundation.repository.JijinSyncFileRepository;
import com.lufax.jijin.scheduler.SchedulerJob;
import com.lufax.jijin.scheduler.SchedulerJobService;
import com.lufax.mq.client.util.MapUtils;
import com.sun.jersey.api.core.InjectParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

/**
 * Created by NiuZhanJun on 7/27/15.
 */
@Path("/v1/wind/file")
public class JijinExFileResource {
    @InjectParam
    private JijinSyncFileRepository jijinSyncFileRepository;
    @InjectParam
    private SchedulerJobService schedulerJobService;
    @InjectParam
    private ScanJijinExFileJob scanJijinExFileJob;

    /**
     * 拉取wind基金信息文件,每10分钟处理1000条
     * @param runId
     * @param runCallbackDest
     * @return
     */
    @GET
    @Path("/pull")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String pullWindFileJob(@QueryParam("runId") final String runId, @QueryParam("runCallbackDest") final String runCallbackDest) {
        return schedulerJobService.executeThread(new SchedulerJob(runId, runCallbackDest, "handleJijinExPullFileJob") {
            @Override
            public void execute() {
                Logger.info(this, "Start the handleJijinExPullFileJob! ");
                scanJijinExFileJob.setMAX_PROCESS_AMOUNT(100); // set max process file number per round
                scanJijinExFileJob.execute();
                Logger.info(this, "End the handleJijinExPullFileJob! ");
            }
        });
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public String addWindFileInfo(@FormParam("path") String filePath, @FormParam("name") String fileName, @FormParam("type") String fileType) {
        Logger.info(this, "call /v1/wind/file/add start.");

        JijinExAddFileResultEnum jijinExAddFileResultEnum = JijinExAddFileResultEnum.SUCCESS;

        try {
            //获取BizType
            String typeName = DaiXiaoFileTypeEnum.getTypeNameByCode(fileType);
            Logger.info(this, String.format("filePath=[%s] fileName = [%s] fileType = [%s] typeName = [%s]", filePath, fileName, fileType, typeName));
            //校验参数，参数不能为空且文件名不能已存在
            jijinExAddFileResultEnum = checkParam(filePath, fileName, fileType, typeName);
            if (jijinExAddFileResultEnum != JijinExAddFileResultEnum.SUCCESS) {
                return jijinExAddFileResultEnum.toJson();
            }

            //新增记录
            JijinSyncFileDTO jijinSyncFileDTO = new JijinSyncFileDTO();
            jijinSyncFileDTO.setSourcePath(filePath);
            jijinSyncFileDTO.setFileName(fileName);
            jijinSyncFileDTO.setBizType(typeName);
            jijinSyncFileDTO.setBizDate(DateUtils.formatDate(new Date(), "yyyyMMdd"));
            jijinSyncFileDTO.setCurrentLine(1l);
            jijinSyncFileDTO.setRetryTimes(0l);
            jijinSyncFileDTO.setStatus(SyncFileStatus.INIT.name());
            jijinSyncFileRepository.insertBusJijinSyncFile(jijinSyncFileDTO);

        } catch (Exception e) {
            Logger.error(this, "get jijin_ex_file_list info  occur Exception !", e);
            jijinExAddFileResultEnum = JijinExAddFileResultEnum.ERROR_EXCEPTION;
        }

        return jijinExAddFileResultEnum.toJson();
    }

    private JijinExAddFileResultEnum checkParam(String filePath, String fileName, String fileType, String typeName) {
        JijinExAddFileResultEnum jijinExAddFileResultEnum = JijinExAddFileResultEnum.SUCCESS;

        //参数不能为空
        if (filePath == null || fileName == null || fileType == null || typeName == null
                || "".equals(filePath) || "".equals(fileName) || "".equals(fileType) || "".equals(typeName)) {
            jijinExAddFileResultEnum = JijinExAddFileResultEnum.ERROR_PARAMETER;
        }

        List<JijinSyncFileDTO> busJijinSyncFileList = jijinSyncFileRepository.findBusJijinSyncFileList(MapUtils.buildKeyValueMap("sourcePath", filePath, "fileName", fileName));

        //同名文件不能已存在
        if (busJijinSyncFileList != null && busJijinSyncFileList.size() > 0) {
            jijinExAddFileResultEnum = JijinExAddFileResultEnum.ERROR_FILE_EXISTED;
        }

        return jijinExAddFileResultEnum;
    }
}
