import { element, by, ElementFinder } from 'protractor';

export class DishTagComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-dish-tag div table .btn-danger'));
  title = element.all(by.css('jhi-dish-tag div h2#page-heading span')).first();
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

export class DishTagUpdatePage {
  pageTitle = element(by.id('jhi-dish-tag-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  dishTagSelect = element(by.id('field_dishTag'));

  dishSelect = element(by.id('field_dish'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDishTagSelect(dishTag: string): Promise<void> {
    await this.dishTagSelect.sendKeys(dishTag);
  }

  async getDishTagSelect(): Promise<string> {
    return await this.dishTagSelect.element(by.css('option:checked')).getText();
  }

  async dishTagSelectLastOption(): Promise<void> {
    await this.dishTagSelect.all(by.tagName('option')).last().click();
  }

  async dishSelectLastOption(): Promise<void> {
    await this.dishSelect.all(by.tagName('option')).last().click();
  }

  async dishSelectOption(option: string): Promise<void> {
    await this.dishSelect.sendKeys(option);
  }

  getDishSelect(): ElementFinder {
    return this.dishSelect;
  }

  async getDishSelectedOption(): Promise<string> {
    return await this.dishSelect.element(by.css('option:checked')).getText();
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

export class DishTagDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-dishTag-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-dishTag'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
