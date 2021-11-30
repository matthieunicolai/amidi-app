import { element, by, ElementFinder } from 'protractor';

export class ClientComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-client div table .btn-danger'));
  title = element.all(by.css('jhi-client div h2#page-heading span')).first();
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

export class ClientUpdatePage {
  pageTitle = element(by.id('jhi-client-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nameInput = element(by.id('field_name'));
  surnameInput = element(by.id('field_surname'));
  birthDateInput = element(by.id('field_birthDate'));
  emailInput = element(by.id('field_email'));
  phoneNumberInput = element(by.id('field_phoneNumber'));
  clientLoginInput = element(by.id('field_clientLogin'));
  clientPwdInput = element(by.id('field_clientPwd'));
  isActivatedInput = element(by.id('field_isActivated'));

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

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setSurnameInput(surname: string): Promise<void> {
    await this.surnameInput.sendKeys(surname);
  }

  async getSurnameInput(): Promise<string> {
    return await this.surnameInput.getAttribute('value');
  }

  async setBirthDateInput(birthDate: string): Promise<void> {
    await this.birthDateInput.sendKeys(birthDate);
  }

  async getBirthDateInput(): Promise<string> {
    return await this.birthDateInput.getAttribute('value');
  }

  async setEmailInput(email: string): Promise<void> {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput(): Promise<string> {
    return await this.emailInput.getAttribute('value');
  }

  async setPhoneNumberInput(phoneNumber: string): Promise<void> {
    await this.phoneNumberInput.sendKeys(phoneNumber);
  }

  async getPhoneNumberInput(): Promise<string> {
    return await this.phoneNumberInput.getAttribute('value');
  }

  async setClientLoginInput(clientLogin: string): Promise<void> {
    await this.clientLoginInput.sendKeys(clientLogin);
  }

  async getClientLoginInput(): Promise<string> {
    return await this.clientLoginInput.getAttribute('value');
  }

  async setClientPwdInput(clientPwd: string): Promise<void> {
    await this.clientPwdInput.sendKeys(clientPwd);
  }

  async getClientPwdInput(): Promise<string> {
    return await this.clientPwdInput.getAttribute('value');
  }

  getIsActivatedInput(): ElementFinder {
    return this.isActivatedInput;
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

export class ClientDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-client-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-client'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
