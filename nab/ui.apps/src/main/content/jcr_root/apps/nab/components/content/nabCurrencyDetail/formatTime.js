'use strict';

use(function() {
    var time = this.value;
    var formattedTime = com.nab.core.NABUtilities.formatTime(time);

    return {
        formattedTime: formattedTime
    };
});
