import { element, by, ElementFinder } from 'protractor';

export class LocationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-location div table .btn-danger'));
  title = element.all(by.css('jhi-location div h2#page-heading span')).first();
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

export class LocationUpdatePage {
  pageTitle = element(by.id('jhi-location-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  countryInput = element(by.id('field_country'));
  cityInput = element(by.id('field_city'));
  postalCodeInput = element(by.id('field_postalCode'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCountryInput(country: string): Promise<void> {
    await this.countryInput.sendKeys(country);
  }

  async getCountryInput(): Promise<string> {
    return await this.countryInput.getAttribute('value');
  }

  async setCityInput(city: string): Promise<void> {
    await this.cityInput.sendKeys(city);
  }

  async getCityInput(): Promise<string> {
    return await this.cityInput.getAttribute('value');
  }

  async setPostalCodeInput(postalCode: string): Promise<void> {
    await this.postalCodeInput.sendKeys(postalCode);
  }

  async getPostalCodeInput(): Promise<string> {
    return await this.postalCodeInput.getAttribute('value');
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

export class LocationDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-location-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-location'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
