import { SimpleViewPage } from './app.po';

describe('simple-view App', () => {
  let page: SimpleViewPage;

  beforeEach(() => {
    page = new SimpleViewPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!!');
  });
});
