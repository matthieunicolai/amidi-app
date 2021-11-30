import { element, by, ElementFinder } from 'protractor';

export class PictureComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-picture div table .btn-danger'));
  title = element.all(by.css('jhi-picture div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class PictureUpdatePage {
  pageTitle = element(by.id('jhi-picture-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  pictureNameInput = element(by.id('field_pictureName'));
  pictureUrlInput = element(by.id('field_pictureUrl'));
  pictureAltInput = element(by.id('field_pictureAlt'));
  isLogoInput = element(by.id('field_isLogo'));
  isDisplayedInput = element(by.id('field_isDisplayed'));

  restaurantSelect = element(by.id('field_restaurant'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setPictureNameInput(pictureName: string): Promise<void> {
    await this.pictureNameInput.sendKeys(pictureName);
  }

  async getPictureNameInput(): Promise<string> {
    return await this.pictureNameInput.getAttribute('value');
  }

  async setPictureUrlInput(pictureUrl: string): Promise<void> {
    await this.pictureUrlInput.sendKeys(pictureUrl);
  }

  async getPictureUrlInput(): Promise<string> {
    return await this.pictureUrlInput.getAttribute('value');
  }

  async setPictureAltInput(pictureAlt: string): Promise<void> {
    await this.pictureAltInput.sendKeys(pictureAlt);
  }

  async getPictureAltInput(): Promise<string> {
    return await this.pictureAltInput.getAttribute('value');
  }

  getIsLogoInput(): ElementFinder {
    return this.isLogoInput;
  }

  getIsDisplayedInput(): ElementFinder {
    return this.isDisplayedInput;
  }

  async restaurantSelectLastOption(): Promise<void> {
    await this.restaurantSelect.all(by.tagName('option')).last().click();
  }

  async restaurantSelectOption(option: string): Promise<void> {
    await this.restaurantSelect.sendKeys(option);
  }

  getRestaurantSelect(): ElementFinder {
    return this.restaurantSelect;
  }

  async getRestaurantSelectedOption(): Promise<string> {
    return await this.restaurantSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class PictureDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-picture-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-picture'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
