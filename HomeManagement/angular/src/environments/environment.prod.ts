
const url = window.location.href;
const hostName = url.split('#')[0];

export const environment = {
  production: false,
  clientId: 'HomeManagement',
  grant_type: 'password',
  scope: 'write',
  clientName: 'HomeManagement',
  client_secret: 'HomeManagement',
  siteKey: '6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI',
  apiUrl: hostName
};
