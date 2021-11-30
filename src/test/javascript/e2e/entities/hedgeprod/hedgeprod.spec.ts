import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { HedgeprodComponentsPage, HedgeprodDeleteDialog, HedgeprodUpdatePage } from './hedgeprod.page-object';

const expect = chai.expect;

describe('Hedgeprod e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let hedgeprodComponentsPage: HedgeprodComponentsPage;
  let hedgeprodUpdatePage: HedgeprodUpdatePage;
  let hedgeprodDeleteDialog: HedgeprodDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Hedgeprods', async () => {
    await navBarPage.goToEntity('hedgeprod');
    hedgeprodComponentsPage = new HedgeprodComponentsPage();
    await browser.wait(ec.visibilityOf(hedgeprodComponentsPage.title), 5000);
    expect(await hedgeprodComponentsPage.getTitle()).to.eq('amidiappApp.hedgeprod.home.title');
    await browser.wait(ec.or(ec.visibilityOf(hedgeprodComponentsPage.entities), ec.visibilityOf(hedgeprodComponentsPage.noResult)), 1000);
  });

  it('should load create Hedgeprod page', async () => {
    await hedgeprodComponentsPage.clickOnCreateButton();
    hedgeprodUpdatePage = new HedgeprodUpdatePage();
    expect(await hedgeprodUpdatePage.getPageTitle()).to.eq('amidiappApp.hedgeprod.home.createOrEditLabel');
    await hedgeprodUpdatePage.cancel();
  });

  it('should create and save Hedgeprods', async () => {
    const nbButtonsBeforeCreate = await hedgeprodComponentsPage.countDeleteButtons();

    await hedgeprodComponentsPage.clickOnCreateButton();

    await promise.all([
      hedgeprodUpdatePage.setHNameInput('hName'),
      hedgeprodUpdatePage.setHSurnameInput('hSurname'),
      hedgeprodUpdatePage.hRoleSelectLastOption(),
      hedgeprodUpdatePage.setHEmailInput('hEmail'),
      hedgeprodUpdatePage.setHPhoneNumberInput('hPhoneNumber'),
      hedgeprodUpdatePage.getIsActivatedInput().click(),
    ]);

    await hedgeprodUpdatePage.save();
    expect(await hedgeprodUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await hedgeprodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Hedgeprod', async () => {
    const nbButtonsBeforeDelete = await hedgeprodComponentsPage.countDeleteButtons();
    await hedgeprodComponentsPage.clickOnLastDeleteButton();

    hedgeprodDeleteDialog = new HedgeprodDeleteDialog();
    expect(await hedgeprodDeleteDialog.getDialogTitle()).to.eq('amidiappApp.hedgeprod.delete.question');
    await hedgeprodDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(hedgeprodComponentsPage.title), 5000);

    expect(await hedgeprodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
