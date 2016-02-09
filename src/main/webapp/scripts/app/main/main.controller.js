'use strict';

angular.module('ifootballApp')
    .controller('MainController', function ($scope, Principal, lastStatus, webNotification, LastStatusHistory, StatusHistoryWebSocket) {

        $scope.autoupdate = false;
        $scope.notify = false;

        $scope.setCss = function (scope) {
            if (scope.lastStatus.status == "FREE") {
                scope.css = "text-success";
            }
            else if (scope.lastStatus.status == "OCCUPIED") {
                scope.css = "text-danger";
            }
            else {
                scope.css = "text-warning";
            }
        }

        $scope.getTime = function() {
            return moment($scope.lastStatus.time).fromNow(true);
        }

        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.notificationTest = function() {
            webNotification.showNotification('Status changed ...', {
                body: 'Status: '+$scope.lastStatus.status+'\n'+$scope.lastStatus.time,
                icon: '../bower_components/HTML5-Desktop-Notifications2/alert.ico',
                onClick: function onNotificationClicked() {
                    window.alert('Notification clicked.');
                },
                autoClose: 4000 //auto close the notification after 2 seconds (you manually close it via hide function)
            }, function onShow(error, hide) {
                if (error) {
                    window.alert('Unable to show notification: ' + error.message);
                } else {
                    console.log('Notification Shown.');
                    setTimeout(function hideNotification() {
                        console.log('Hiding notification....');
                        hide(); //manually close the notification (or let the autoClose close it)
                    }, 5000);
                }
            });
        }

        var updateStatus = function (data) {
            $scope.lastStatus = data;
            $scope.setCss($scope);
            if ($scope.notify) {
                $scope.notificationTest();
            }
        }

        $scope.refresh = function() {
            updateStatus(LastStatusHistory.get());
        }

        $scope.$watch('autoupdate', function(newValue, oldValue) {
            if (newValue) {
                StatusHistoryWebSocket.connect();
                StatusHistoryWebSocket.subscribe();
                StatusHistoryWebSocket.receive().then(null, null, updateStatus);
            } else {
                StatusHistoryWebSocket.unsubscribe();
                StatusHistoryWebSocket.disconnect();
            }
        })

        updateStatus(lastStatus);
    });
