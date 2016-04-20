package com.lufax.jijin.fundation.service;

import com.lufax.jijin.base.service.TradeDayService;
import com.lufax.jijin.base.utils.*;
import com.lufax.jijin.fundation.constant.SyncFileBizType;
import com.lufax.jijin.fundation.repository.JijinInfoRepository;
import com.lufax.jijin.fundation.service.domain.FileHolder;
import com.lufax.jijin.fundation.util.DahuaDecrptyUtils;
import com.lufax.kernel.security.kms.api.KmsService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
public class ScanServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private ScanService service;

    @Autowired
    private JijinAppProperties jijinAppProperties;
    @Autowired
    private TradeDayService tradeDayService;
    @Autowired
    private JijinInfoRepository jijinInfoRepository;
    @Autowired
    private JijinInstIdPlatMerchantIdMapHolder jijinInstIdPlatMerchantIdMapHolder;
    @Autowired
    private KmsService kmsService;

    @Test
    public void testIncreaseDividend() {

        Date date = new Date();
        String rootPath = jijinAppProperties.getJijinNasRootDir();
        String filePath = rootPath + "upload/";
        String fileName = "FUND_RESULT_P" + "_商户号_" + DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT) + "_1.txt";

        FileHolder fileHolder = new FileHolder();
        fileHolder.setFileAbsolutePath(filePath);
        fileHolder.setFileName(fileName);

        List<FileHolder> fileHolderList = new ArrayList<FileHolder>();
        fileHolderList.add(fileHolder);

        service.yiTiaoLong(fileHolderList, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), SyncFileBizType.PAF_REDEEM_DIVIDEND.name());

	}
	
	@Test
	public void testScanAudit(){
		Date date = new Date();
        if (tradeDayService.isTradeDay(date)) {
            Logger.info(this, "Start scan jijin audit file!");
            List<String> instIds = jijinInfoRepository.getDistinctInstId();
            String dateStr = DateUtils.formatDateAsString(date);
            List<FileHolder> pafFileHolderList = new ArrayList<FileHolder>();
            List<FileHolder> jijinBuyFileHolderList = new ArrayList<FileHolder>();
            List<FileHolder> jijinRedeemFileHolderList = new ArrayList<FileHolder>();
            List<FileHolder> jijinUserBalanceFileHolderList = new ArrayList<FileHolder>();

            for (String instId : instIds) {

                String saleCode = jijinInstIdPlatMerchantIdMapHolder.getFundSaleCode(instId);

                FileHolder fileHolder1 = new FileHolder();
                fileHolder1.setFileName(saleCode + "_" + dateStr + "_to_bank_fund_txn_10.txt");
                fileHolder1.setFileAbsolutePath(jijinAppProperties.getJijinNasRootDir() + instId + "/download/" + dateStr + "/");

                pafFileHolderList.add(fileHolder1);

                FileHolder fileHolder2 = new FileHolder();
                fileHolder2.setFileName(saleCode + "_" + dateStr + "_to_bank_fund_txn_10.txt");
                fileHolder2.setFileAbsolutePath(jijinAppProperties.getJijinNasRootDir() + instId + "/upload/" + dateStr + "/");

                jijinBuyFileHolderList.add(fileHolder2);


                FileHolder fileHolder3 = new FileHolder();
                fileHolder3.setFileName(saleCode + "_" + dateStr + "_to_bank_fund_txn_12.txt");
                fileHolder3.setFileAbsolutePath(jijinAppProperties.getJijinNasRootDir() + instId + "/upload/" + dateStr + "/");

                jijinRedeemFileHolderList.add(fileHolder3);


                FileHolder fileHolder4 = new FileHolder();
                fileHolder4.setFileName(instId + "_" + dateStr + "_06.txt");
                fileHolder4.setFileAbsolutePath(jijinAppProperties.getJijinNasRootDir() + instId + "/upload/" + dateStr + "/");

                jijinUserBalanceFileHolderList.add(fileHolder4);

            }

            service.yiTiaoLong(pafFileHolderList, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), SyncFileBizType.PAF_BUY_AUDIT.name());
            service.yiTiaoLong(jijinBuyFileHolderList, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), SyncFileBizType.JIJIN_BUY_AUDIT.name());
            service.yiTiaoLong(jijinRedeemFileHolderList, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), SyncFileBizType.JIJIN_REDEEM_AUDIT.name());
            service.yiTiaoLong(jijinUserBalanceFileHolderList, DateUtils.formatDate(date, DateUtils.DATE_STRING_FORMAT), SyncFileBizType.JIJIN_BALANCE_AUDIT.name());

            Logger.info(this, "End scan jijin audit file!");
        }
    }


    @Test
    public void testScanYitiaoLongForZip() {
    	
    	List<FileHolder> fileList = new ArrayList<FileHolder>();
    	FileHolder fileHolder = new FileHolder();
    	fileHolder.setFileAbsolutePath("file/");
    	fileHolder.setFileName("upload_testFile.zip");
    	fileList.add(fileHolder);
    	
    	File f = FileUtils.createEmptyFile("file/upload_testFile");
    	FileUtils.appendContent(f, "content test!!");
    	FileUtils.compress("file/", "file/upload_testFile.zip", "upload_testFile");
    	    	
    	service.yiTiaoLongForZIP(fileList, "20970101", "bizType");
    	
    	FileUtils.forceDelete(f);
    	
//    	FileUtils.forceDelete(new File("file/download_testFile.zip"));
//    	FileUtils.forceDelete(new File("file/upload_testFile.zip"));
    }

    public void checkAndMkdir(String dir) {
        File pullDir = new File(dir);
        if (!pullDir.exists() || !pullDir.isDirectory()) {
            pullDir.mkdirs();
        }
    }


}
