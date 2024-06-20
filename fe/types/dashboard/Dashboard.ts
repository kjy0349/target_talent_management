export interface Dashboard {
  // salesThisWeek: DashboardSalesThisWeek;
  // newProductsThisWeek: DashboardNewProductsThisWeek;
  visitorsThisWeek: DashboardVisitorsThisWeek;
  userSignupsThisWeek: DashboardUserSignupsThisWeek;
  sessionsByCountryMap: DashboardSessionsByCountryMap;
  // latestCustomers: DashboardLatestCustomer[];
  acquisitionOverview: DashboardAcquisitionOverview;
  // transactions: Transaction[];
  mainPageContent: DashboardMainContent;
}

export interface AdminDashboard {
  salesThisWeek: DashboardSalesThisWeek;
  cwidth: number;
}
export interface DashboardMainContentFull {
  acquisitionOverview: DashboardAcquisitionOverview;
  mainPageContent: DashboardMainContent;
}
export interface DashboardMainContent {
  totalPoolSize: number;
  ePollSize: number;
  developerPoolSize: number;
  pPollSizePoolSize: number;
  networkingPoolSize: number;
  techmapPoolSize: number;
}

export interface DashboardSalesThisWeek {
  sales: number;
  percentage: number;
  categories: string[];
  series: DashboardSalesThisWeekSeries[];
}

export interface DashboardSalesThisWeekSeries {
  name: string;
  data: number[];
  color: string;
}

export interface DashboardNewProductsThisWeek {
  products: number;
  percentage: number;
  series: DashboardNewProductsThisWeekSeries[];
}

export interface DashboardNewProductsThisWeekSeries {
  name: string;
  color: string;
  data: {
    x: string;
    y: number;
  }[];
}

export interface DashboardVisitorsThisWeek {
  visitors: number;
  percentage: number;
  labels: string[];
  series: DashboardVisitorsThisWeekSeries[];
}

export interface DashboardVisitorsThisWeekSeries {
  name: string;
  data: number[];
}

export interface DashboardUserSignupsThisWeek {
  signups: number;
  percentage: number;
  labels: string[];
  series: DashboardUserSignupsThisWeekSeries[];
}

export interface DashboardUserSignupsThisWeekSeries {
  name: string;
  data: number[];
}

export interface DashboardSessionsByCountryMap {
  [country: string]: {
    visitors: number;
    change: number;
    korName?: string;
  };
}

export interface DashboardLatestCustomer {
  name: string;
  avatar: string;
  email: string;
  spent: number;
}

export interface DashboardAcquisitionOverview {
  labels: string[];
  series: number[];
  topChannels?: DashboardAcquisitionOverviewTopChannel[];
}

export interface DashboardAcquisitionOverviewTopChannel {
  channel: string;
  users: number;
  acquisition: number;
}

export interface Transaction {
  transaction: string;
  date: string;
  amount: number;
  status: string;
}

export interface DashboardDepartmentSearchConditionDto {
  jobRankId: number;
  skillMainCategory: string;
}
export interface DashboardMonthlySearchConditionDto {
  createdYear: number;
  departmentId: number;
}

export interface DashbaordCountrySearchConditionDto {
  viewYear: number;
  viewMonth: number;
}
