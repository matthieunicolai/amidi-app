import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ProUserComponentsPage, ProUserDeleteDialog, ProUserUpdatePage } from './pro-user.page-object';

const expect = chai.expect;

describe('ProUser e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let proUserComponentsPage: ProUserComponentsPage;
  let proUserUpdatePage: ProUserUpdatePage;
  let proUserDeleteDialog: ProUserDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ProUsers', async () => {
    await navBarPage.goToEntity('pro-user');
    proUserComponentsPage = new ProUserComponentsPage();
    await browser.wait(ec.visibilityOf(proUserComponentsPage.title), 5000);
    expect(await proUserComponentsPage.getTitle()).to.eq('amidiappApp.proUser.home.title');
    await browser.wait(ec.or(ec.visibilityOf(proUserComponentsPage.entities), ec.visibilityOf(proUserComponentsPage.noResult)), 1000);
  });

  it('should load create ProUser page', async () => {
    await proUserComponentsPage.clickOnCreateButton();
    proUserUpdatePage = new ProUserUpdatePage();
    expect(await proUserUpdatePage.getPageTitle()).to.eq('amidiappApp.proUser.home.createOrEditLabel');
    await proUserUpdatePage.cancel();
  });

  it('should create and save ProUsers', async () => {
    const nbButtonsBeforeCreate = await proUserComponentsPage.countDeleteButtons();

    await proUserComponentsPage.clickOnCreateButton();

    await promise.all([
      proUserUpdatePage.setProUserNameInput('proUserName'),
      proUserUpdatePage.setProUserSurnameInput('proUserSurname'),
      proUserUpdatePage.proUserRoleSelectLastOption(),
      proUserUpdatePage.setProUserLoginInput('proUserLogin'),
      proUserUpdatePage.setProUserPwdInput('proUserPwd'),
      proUserUpdatePage.setProUserEmailInput('proUserEmail'),
      proUserUpdatePage.setProUserPhoneNumberInput('proUserPhoneNumber'),
      proUserUpdatePage.getIsActivatedInput().click(),
      proUserUpdatePage.restaurantSelectLastOption(),
      proUserUpdatePage.restaurantSelectLastOption(),
    ]);

    await proUserUpdatePage.save();
    expect(await proUserUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await proUserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last ProUser', async () => {
    const nbButtonsBeforeDelete = await proUserComponentsPage.countDeleteButtons();
    await proUserComponentsPage.clickOnLastDeleteButton();

    proUserDeleteDialog = new ProUserDeleteDialog();
    expect(await proUserDeleteDialog.getDialogTitle()).to.eq('amidiappApp.proUser.delete.question');
    await proUserDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(proUserComponentsPage.title), 5000);

    expect(await proUserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
