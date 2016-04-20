package com.lufax.jijin.base.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JijinAppProperties {


    @Value("${ylx.request.nas.root.dir}")
    private String ylxRequestRootDir;

    @Value("${ylx.response.nas.root.dir}")
    private String ylxResponseRootDir;

    @Value("${gw.ylx.service.url}")
    private String gwYlxServiceUrl;

    @Value("${ylx.batch.size}")
    private String ylxBatchSize;

    @Value("${ylx.max.trade.confirm.buy.retry.times}")
    private String maxTradeConfirmBuyRetryTimes;

    @Value("${ylx.trad.confirm.batch.size}")
    private String ylxTradeConfirmBatchSize;

    @Value("${ylx.batch.file.max.size}")
    private String ylxFileMaxSize;

	@Value("${ylx.max.batch.hang.limit.minutes}")
	private String maxBatchHangLimit;

    @Value("${ylx.batch.retry.limit}")
    private String ylxBatchRetryLimit;

	@Value("${ylx.open.buy.request.timeout}")
	private String openBuyRequestTimeout;

    @Value("${ylx.sell.request.timeout}")
    private String sellRequestTimeout;

    @Value("${ylx.max.confirm.job.running.days}")
    private String ylxMaxConfirmJobRunningDays;

    @Value("${ylx.pull.confirm.timeout}")
    private String ylxPullConfirmTimeout;
    

    @Value("${gw.jijin.service.url}")
    private String gwJijinServiceUrl;
    @Value("${nas.jijin.root.dir}")
    private String jijinNasRootDir;
    @Value("${nas.wind.root.dir}")
    private String jijinExNasRootDir;
    @Value("${environment}")
    private String environment;
    @Value("${salecode.map}")
    private String saleCodeMap;

    
    @Value("${dahua.3des}")
    private String dahua3des;

    @Value("${lufax.coin}")
    private String lufaxCoin;

	public String getLufaxCoin() {
		return lufaxCoin;
	}

	public String getDahua3des() {
		return dahua3des;
	}

	public String getSellRequestTimeout() {
		return sellRequestTimeout;
	}

    public String getJijinNasRootDir() {
        return jijinNasRootDir;
    }

    public String getJijinExNasRootDir() {
        return jijinExNasRootDir;
    }

    public String getOpenBuyRequestTimeout() {
        return openBuyRequestTimeout;
    }


	public String getYlxFileMaxSize() {
		return ylxFileMaxSize;
	}

    public String getGwJijinServiceUrl() {
        return gwJijinServiceUrl;
    }


    public String getYlxRequestRootDir() {
        return ylxRequestRootDir;
    }

    public String getYlxResponseRootDir() {
        return ylxResponseRootDir;
    }


	public String getGwYlxServiceUrl() {
		return gwYlxServiceUrl;
	}

	public Long getYlxBatchSize() {
		return Long.valueOf(ylxBatchSize);
	}


    public Long getMaxTradeConfirmBuyRetryTimes() {
        return Long.valueOf(maxTradeConfirmBuyRetryTimes);
    }

    public Long getYlxTradeConfirmBatchSize() {
        return Long.valueOf(ylxTradeConfirmBatchSize);
    }


    public long getMaxBatchHangLimit() {
        return Long.valueOf(maxBatchHangLimit);
    }

    public long getYlxBatchRetryLimit() {
        return Long.valueOf(ylxBatchRetryLimit);
    }

    public long getYlxMaxConfirmJobRunningDays() {
        return Long.valueOf(ylxMaxConfirmJobRunningDays);
    }

    public String getEnvironment() {
        return environment;
    }

	public String getSaleCodeMap() {
		return saleCodeMap;
	}

    public String getYlxPullConfirmTimeout() {
        return ylxPullConfirmTimeout;
    }

}
