import { element, by, ElementFinder } from 'protractor';

export class HedgeprodComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-hedgeprod div table .btn-danger'));
  title = element.all(by.css('jhi-hedgeprod div h2#page-heading span')).first();
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

export class HedgeprodUpdatePage {
  pageTitle = element(by.id('jhi-hedgeprod-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  hNameInput = element(by.id('field_hName'));
  hSurnameInput = element(by.id('field_hSurname'));
  hRoleSelect = element(by.id('field_hRole'));
  hEmailInput = element(by.id('field_hEmail'));
  hPhoneNumberInput = element(by.id('field_hPhoneNumber'));
  isActivatedInput = element(by.id('field_isActivated'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setHNameInput(hName: string): Promise<void> {
    await this.hNameInput.sendKeys(hName);
  }

  async getHNameInput(): Promise<string> {
    return await this.hNameInput.getAttribute('value');
  }

  async setHSurnameInput(hSurname: string): Promise<void> {
    await this.hSurnameInput.sendKeys(hSurname);
  }

  async getHSurnameInput(): Promise<string> {
    return await this.hSurnameInput.getAttribute('value');
  }

  async setHRoleSelect(hRole: string): Promise<void> {
    await this.hRoleSelect.sendKeys(hRole);
  }

  async getHRoleSelect(): Promise<string> {
    return await this.hRoleSelect.element(by.css('option:checked')).getText();
  }

  async hRoleSelectLastOption(): Promise<void> {
    await this.hRoleSelect.all(by.tagName('option')).last().click();
  }

  async setHEmailInput(hEmail: string): Promise<void> {
    await this.hEmailInput.sendKeys(hEmail);
  }

  async getHEmailInput(): Promise<string> {
    return await this.hEmailInput.getAttribute('value');
  }

  async setHPhoneNumberInput(hPhoneNumber: string): Promise<void> {
    await this.hPhoneNumberInput.sendKeys(hPhoneNumber);
  }

  async getHPhoneNumberInput(): Promise<string> {
    return await this.hPhoneNumberInput.getAttribute('value');
  }

  getIsActivatedInput(): ElementFinder {
    return this.isActivatedInput;
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

export class HedgeprodDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-hedgeprod-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-hedgeprod'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
