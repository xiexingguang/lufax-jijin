package com.lufax.jijin.fundation.service;

import org.junit.Ignore;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

@TransactionConfiguration(defaultRollback = true)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml", "file:src/main/resources/dataSource.xml"})
@Ignore
public class AccountServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

//	@Autowired
//	private AccountService accountService;
//	@Autowired
//	private JunitMockModelBuilder mockBuilder;
//    @Autowired
//    private UserService userService;
//
//	@Before
//	public void setUp() {
//		// set mock
//		userService = mock(UserService.class);
//		accountService.setUserService(userService);
//		mockBuilder.buildJijinUserAccount(1l, "yfd101");
//		mockBuilder.buildJijinUserBalance(1l,"470009");
//		mockBuilder.buildJijinInfo("470009","yfd101","stock");
//	}
//
//	@Test
//	public void testCheckUser(){
//		assertTrue(accountService.checkUser(1l, "yfd101"));
//	}
//
//	@Test
//	public void testCheckUserForProd(){
//
//		JijinExCharacterDTO dto = new JijinExCharacterDTO();
//		UserInfoGson value = new UserInfoGson();
//		when(userService.getUserInfo(Mockito.anyLong())).thenReturn(value);
//		assertTrue(accountService.checkUserForProd(1l, dto));
//	}
//
//	@Test
//	public void testCheckUserForPurchase(){
//
//		UserInfoGson value = new UserInfoGson();
//		when(userService.getUserInfo(Mockito.anyLong())).thenReturn(value);
//		assertTrue(accountService.checkUserForPurchase(1l, "lfx201"));
//	}
//

}
