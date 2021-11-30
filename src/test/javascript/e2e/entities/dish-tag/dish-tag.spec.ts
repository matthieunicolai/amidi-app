import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DishTagComponentsPage, DishTagDeleteDialog, DishTagUpdatePage } from './dish-tag.page-object';

const expect = chai.expect;

describe('DishTag e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let dishTagComponentsPage: DishTagComponentsPage;
  let dishTagUpdatePage: DishTagUpdatePage;
  let dishTagDeleteDialog: DishTagDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load DishTags', async () => {
    await navBarPage.goToEntity('dish-tag');
    dishTagComponentsPage = new DishTagComponentsPage();
    await browser.wait(ec.visibilityOf(dishTagComponentsPage.title), 5000);
    expect(await dishTagComponentsPage.getTitle()).to.eq('amidiappApp.dishTag.home.title');
    await browser.wait(ec.or(ec.visibilityOf(dishTagComponentsPage.entities), ec.visibilityOf(dishTagComponentsPage.noResult)), 1000);
  });

  it('should load create DishTag page', async () => {
    await dishTagComponentsPage.clickOnCreateButton();
    dishTagUpdatePage = new DishTagUpdatePage();
    expect(await dishTagUpdatePage.getPageTitle()).to.eq('amidiappApp.dishTag.home.createOrEditLabel');
    await dishTagUpdatePage.cancel();
  });

  it('should create and save DishTags', async () => {
    const nbButtonsBeforeCreate = await dishTagComponentsPage.countDeleteButtons();

    await dishTagComponentsPage.clickOnCreateButton();

    await promise.all([dishTagUpdatePage.dishTagSelectLastOption(), dishTagUpdatePage.dishSelectLastOption()]);

    await dishTagUpdatePage.save();
    expect(await dishTagUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await dishTagComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last DishTag', async () => {
    const nbButtonsBeforeDelete = await dishTagComponentsPage.countDeleteButtons();
    await dishTagComponentsPage.clickOnLastDeleteButton();

    dishTagDeleteDialog = new DishTagDeleteDialog();
    expect(await dishTagDeleteDialog.getDialogTitle()).to.eq('amidiappApp.dishTag.delete.question');
    await dishTagDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(dishTagComponentsPage.title), 5000);

    expect(await dishTagComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
