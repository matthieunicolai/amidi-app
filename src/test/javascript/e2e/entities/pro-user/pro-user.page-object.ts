import { element, by, ElementFinder } from 'protractor';

export class ProUserComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-pro-user div table .btn-danger'));
  title = element.all(by.css('jhi-pro-user div h2#page-heading span')).first();
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

export class ProUserUpdatePage {
  pageTitle = element(by.id('jhi-pro-user-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  proUserNameInput = element(by.id('field_proUserName'));
  proUserSurnameInput = element(by.id('field_proUserSurname'));
  proUserRoleSelect = element(by.id('field_proUserRole'));
  proUserLoginInput = element(by.id('field_proUserLogin'));
  proUserPwdInput = element(by.id('field_proUserPwd'));
  proUserEmailInput = element(by.id('field_proUserEmail'));
  proUserPhoneNumberInput = element(by.id('field_proUserPhoneNumber'));
  isActivatedInput = element(by.id('field_isActivated'));

  restaurantSelect = element(by.id('field_restaurant'));
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

  async setProUserNameInput(proUserName: string): Promise<void> {
    await this.proUserNameInput.sendKeys(proUserName);
  }

  async getProUserNameInput(): Promise<string> {
    return await this.proUserNameInput.getAttribute('value');
  }

  async setProUserSurnameInput(proUserSurname: string): Promise<void> {
    await this.proUserSurnameInput.sendKeys(proUserSurname);
  }

  async getProUserSurnameInput(): Promise<string> {
    return await this.proUserSurnameInput.getAttribute('value');
  }

  async setProUserRoleSelect(proUserRole: string): Promise<void> {
    await this.proUserRoleSelect.sendKeys(proUserRole);
  }

  async getProUserRoleSelect(): Promise<string> {
    return await this.proUserRoleSelect.element(by.css('option:checked')).getText();
  }

  async proUserRoleSelectLastOption(): Promise<void> {
    await this.proUserRoleSelect.all(by.tagName('option')).last().click();
  }

  async setProUserLoginInput(proUserLogin: string): Promise<void> {
    await this.proUserLoginInput.sendKeys(proUserLogin);
  }

  async getProUserLoginInput(): Promise<string> {
    return await this.proUserLoginInput.getAttribute('value');
  }

  async setProUserPwdInput(proUserPwd: string): Promise<void> {
    await this.proUserPwdInput.sendKeys(proUserPwd);
  }

  async getProUserPwdInput(): Promise<string> {
    return await this.proUserPwdInput.getAttribute('value');
  }

  async setProUserEmailInput(proUserEmail: string): Promise<void> {
    await this.proUserEmailInput.sendKeys(proUserEmail);
  }

  async getProUserEmailInput(): Promise<string> {
    return await this.proUserEmailInput.getAttribute('value');
  }

  async setProUserPhoneNumberInput(proUserPhoneNumber: string): Promise<void> {
    await this.proUserPhoneNumberInput.sendKeys(proUserPhoneNumber);
  }

  async getProUserPhoneNumberInput(): Promise<string> {
    return await this.proUserPhoneNumberInput.getAttribute('value');
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

export class ProUserDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-proUser-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-proUser'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
