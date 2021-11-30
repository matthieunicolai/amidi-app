import { element, by, ElementFinder } from 'protractor';

export class DishComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-dish div table .btn-danger'));
  title = element.all(by.css('jhi-dish div h2#page-heading span')).first();
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

export class DishUpdatePage {
  pageTitle = element(by.id('jhi-dish-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  dishNameInput = element(by.id('field_dishName'));
  dishDescriptionInput = element(by.id('field_dishDescription'));
  dishPriceInput = element(by.id('field_dishPrice'));
  dishDateInput = element(by.id('field_dishDate'));
  dishPictureNameInput = element(by.id('field_dishPictureName'));
  dishPictureUrlInput = element(by.id('field_dishPictureUrl'));
  dishPictureAltInput = element(by.id('field_dishPictureAlt'));
  isDailyDishInput = element(by.id('field_isDailyDish'));
  isAvailableInput = element(by.id('field_isAvailable'));

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

  async setDishNameInput(dishName: string): Promise<void> {
    await this.dishNameInput.sendKeys(dishName);
  }

  async getDishNameInput(): Promise<string> {
    return await this.dishNameInput.getAttribute('value');
  }

  async setDishDescriptionInput(dishDescription: string): Promise<void> {
    await this.dishDescriptionInput.sendKeys(dishDescription);
  }

  async getDishDescriptionInput(): Promise<string> {
    return await this.dishDescriptionInput.getAttribute('value');
  }

  async setDishPriceInput(dishPrice: string): Promise<void> {
    await this.dishPriceInput.sendKeys(dishPrice);
  }

  async getDishPriceInput(): Promise<string> {
    return await this.dishPriceInput.getAttribute('value');
  }

  async setDishDateInput(dishDate: string): Promise<void> {
    await this.dishDateInput.sendKeys(dishDate);
  }

  async getDishDateInput(): Promise<string> {
    return await this.dishDateInput.getAttribute('value');
  }

  async setDishPictureNameInput(dishPictureName: string): Promise<void> {
    await this.dishPictureNameInput.sendKeys(dishPictureName);
  }

  async getDishPictureNameInput(): Promise<string> {
    return await this.dishPictureNameInput.getAttribute('value');
  }

  async setDishPictureUrlInput(dishPictureUrl: string): Promise<void> {
    await this.dishPictureUrlInput.sendKeys(dishPictureUrl);
  }

  async getDishPictureUrlInput(): Promise<string> {
    return await this.dishPictureUrlInput.getAttribute('value');
  }

  async setDishPictureAltInput(dishPictureAlt: string): Promise<void> {
    await this.dishPictureAltInput.sendKeys(dishPictureAlt);
  }

  async getDishPictureAltInput(): Promise<string> {
    return await this.dishPictureAltInput.getAttribute('value');
  }

  getIsDailyDishInput(): ElementFinder {
    return this.isDailyDishInput;
  }

  getIsAvailableInput(): ElementFinder {
    return this.isAvailableInput;
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

export class DishDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-dish-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-dish'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
