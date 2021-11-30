import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PictureComponentsPage, PictureDeleteDialog, PictureUpdatePage } from './picture.page-object';

const expect = chai.expect;

describe('Picture e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let pictureComponentsPage: PictureComponentsPage;
  let pictureUpdatePage: PictureUpdatePage;
  let pictureDeleteDialog: PictureDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Pictures', async () => {
    await navBarPage.goToEntity('picture');
    pictureComponentsPage = new PictureComponentsPage();
    await browser.wait(ec.visibilityOf(pictureComponentsPage.title), 5000);
    expect(await pictureComponentsPage.getTitle()).to.eq('amidiappApp.picture.home.title');
    await browser.wait(ec.or(ec.visibilityOf(pictureComponentsPage.entities), ec.visibilityOf(pictureComponentsPage.noResult)), 1000);
  });

  it('should load create Picture page', async () => {
    await pictureComponentsPage.clickOnCreateButton();
    pictureUpdatePage = new PictureUpdatePage();
    expect(await pictureUpdatePage.getPageTitle()).to.eq('amidiappApp.picture.home.createOrEditLabel');
    await pictureUpdatePage.cancel();
  });

  it('should create and save Pictures', async () => {
    const nbButtonsBeforeCreate = await pictureComponentsPage.countDeleteButtons();

    await pictureComponentsPage.clickOnCreateButton();

    await promise.all([
      pictureUpdatePage.setPictureNameInput('pictureName'),
      pictureUpdatePage.setPictureUrlInput('pictureUrl'),
      pictureUpdatePage.setPictureAltInput('pictureAlt'),
      pictureUpdatePage.getIsLogoInput().click(),
      pictureUpdatePage.getIsDisplayedInput().click(),
      pictureUpdatePage.restaurantSelectLastOption(),
    ]);

    await pictureUpdatePage.save();
    expect(await pictureUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await pictureComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Picture', async () => {
    const nbButtonsBeforeDelete = await pictureComponentsPage.countDeleteButtons();
    await pictureComponentsPage.clickOnLastDeleteButton();

    pictureDeleteDialog = new PictureDeleteDialog();
    expect(await pictureDeleteDialog.getDialogTitle()).to.eq('amidiappApp.picture.delete.question');
    await pictureDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(pictureComponentsPage.title), 5000);

    expect(await pictureComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
