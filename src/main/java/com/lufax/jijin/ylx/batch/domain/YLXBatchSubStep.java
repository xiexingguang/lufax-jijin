package com.lufax.jijin.ylx.batch.domain;

public enum YLXBatchSubStep {
	sub_step_1_1,//update bath id
	sub_step_1_2,//fill userinfo/accountinfo
	sub_step_1_3,//insert file records
	sub_step_1_4,//create files
	sub_step_1_5,//update batch status
	sub_step_2_1,//send file
	sub_step_2_2,//insert confirm batch record
	sub_step_complete, 
	sub_step_4_1, // genearte sms notify operations in sell or trade trx confirm in buy 
	sub_step_4_2,  //send confirm audit notify sms in sell or wait cutoff confirm in buy
	sub_step_4_3   //one ylx withdraw in buy

}
