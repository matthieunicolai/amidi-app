import { element, by, ElementFinder } from 'protractor';

export class RestaurantComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-restaurant div table .btn-danger'));
  title = element.all(by.css('jhi-restaurant div h2#page-heading span')).first();
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

export class RestaurantUpdatePage {
  pageTitle = element(by.id('jhi-restaurant-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  restaurantNameInput = element(by.id('field_restaurantName'));
  restaurantAddressInput = element(by.id('field_restaurantAddress'));
  restaurantAddressCmpInput = element(by.id('field_restaurantAddressCmp'));
  restaurantTypeSelect = element(by.id('field_restaurantType'));
  isSubInput = element(by.id('field_isSub'));
  restaurantSubscriptionSelect = element(by.id('field_restaurantSubscription'));
  restaurantSubDateInput = element(by.id('field_restaurantSubDate'));
  restaurantDescriptionInput = element(by.id('field_restaurantDescription'));
  restaurantPhoneNumberInput = element(by.id('field_restaurantPhoneNumber'));
  restaurantEmailInput = element(by.id('field_restaurantEmail'));
  restaurantWebSiteInput = element(by.id('field_restaurantWebSite'));
  restaurantLatitudeInput = element(by.id('field_restaurantLatitude'));
  restaurantLongitudeInput = element(by.id('field_restaurantLongitude'));
  isActivatedInput = element(by.id('field_isActivated'));

  locationSelect = element(by.id('field_location'));
  locationSelect = element(by.id('field_location'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setRestaurantNameInput(restaurantName: string): Promise<void> {
    await this.restaurantNameInput.sendKeys(restaurantName);
  }

  async getRestaurantNameInput(): Promise<string> {
    return await this.restaurantNameInput.getAttribute('value');
  }

  async setRestaurantAddressInput(restaurantAddress: string): Promise<void> {
    await this.restaurantAddressInput.sendKeys(restaurantAddress);
  }

  async getRestaurantAddressInput(): Promise<string> {
    return await this.restaurantAddressInput.getAttribute('value');
  }

  async setRestaurantAddressCmpInput(restaurantAddressCmp: string): Promise<void> {
    await this.restaurantAddressCmpInput.sendKeys(restaurantAddressCmp);
  }

  async getRestaurantAddressCmpInput(): Promise<string> {
    return await this.restaurantAddressCmpInput.getAttribute('value');
  }

  async setRestaurantTypeSelect(restaurantType: string): Promise<void> {
    await this.restaurantTypeSelect.sendKeys(restaurantType);
  }

  async getRestaurantTypeSelect(): Promise<string> {
    return await this.restaurantTypeSelect.element(by.css('option:checked')).getText();
  }

  async restaurantTypeSelectLastOption(): Promise<void> {
    await this.restaurantTypeSelect.all(by.tagName('option')).last().click();
  }

  getIsSubInput(): ElementFinder {
    return this.isSubInput;
  }

  async setRestaurantSubscriptionSelect(restaurantSubscription: string): Promise<void> {
    await this.restaurantSubscriptionSelect.sendKeys(restaurantSubscription);
  }

  async getRestaurantSubscriptionSelect(): Promise<string> {
    return await this.restaurantSubscriptionSelect.element(by.css('option:checked')).getText();
  }

  async restaurantSubscriptionSelectLastOption(): Promise<void> {
    await this.restaurantSubscriptionSelect.all(by.tagName('option')).last().click();
  }

  async setRestaurantSubDateInput(restaurantSubDate: string): Promise<void> {
    await this.restaurantSubDateInput.sendKeys(restaurantSubDate);
  }

  async getRestaurantSubDateInput(): Promise<string> {
    return await this.restaurantSubDateInput.getAttribute('value');
  }

  async setRestaurantDescriptionInput(restaurantDescription: string): Promise<void> {
    await this.restaurantDescriptionInput.sendKeys(restaurantDescription);
  }

  async getRestaurantDescriptionInput(): Promise<string> {
    return await this.restaurantDescriptionInput.getAttribute('value');
  }

  async setRestaurantPhoneNumberInput(restaurantPhoneNumber: string): Promise<void> {
    await this.restaurantPhoneNumberInput.sendKeys(restaurantPhoneNumber);
  }

  async getRestaurantPhoneNumberInput(): Promise<string> {
    return await this.restaurantPhoneNumberInput.getAttribute('value');
  }

  async setRestaurantEmailInput(restaurantEmail: string): Promise<void> {
    await this.restaurantEmailInput.sendKeys(restaurantEmail);
  }

  async getRestaurantEmailInput(): Promise<string> {
    return await this.restaurantEmailInput.getAttribute('value');
  }

  async setRestaurantWebSiteInput(restaurantWebSite: string): Promise<void> {
    await this.restaurantWebSiteInput.sendKeys(restaurantWebSite);
  }

  async getRestaurantWebSiteInput(): Promise<string> {
    return await this.restaurantWebSiteInput.getAttribute('value');
  }

  async setRestaurantLatitudeInput(restaurantLatitude: string): Promise<void> {
    await this.restaurantLatitudeInput.sendKeys(restaurantLatitude);
  }

  async getRestaurantLatitudeInput(): Promise<string> {
    return await this.restaurantLatitudeInput.getAttribute('value');
  }

  async setRestaurantLongitudeInput(restaurantLongitude: string): Promise<void> {
    await this.restaurantLongitudeInput.sendKeys(restaurantLongitude);
  }

  async getRestaurantLongitudeInput(): Promise<string> {
    return await this.restaurantLongitudeInput.getAttribute('value');
  }

  getIsActivatedInput(): ElementFinder {
    return this.isActivatedInput;
  }

  async locationSelectLastOption(): Promise<void> {
    await this.locationSelect.all(by.tagName('option')).last().click();
  }

  async locationSelectOption(option: string): Promise<void> {
    await this.locationSelect.sendKeys(option);
  }

  getLocationSelect(): ElementFinder {
    return this.locationSelect;
  }

  async getLocationSelectedOption(): Promise<string> {
    return await this.locationSelect.element(by.css('option:checked')).getText();
  }

  async locationSelectLastOption(): Promise<void> {
    await this.locationSelect.all(by.tagName('option')).last().click();
  }

  async locationSelectOption(option: string): Promise<void> {
    await this.locationSelect.sendKeys(option);
  }

  getLocationSelect(): ElementFinder {
    return this.locationSelect;
  }

  async getLocationSelectedOption(): Promise<string> {
    return await this.locationSelect.element(by.css('option:checked')).getText();
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

export class RestaurantDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-restaurant-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-restaurant'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
