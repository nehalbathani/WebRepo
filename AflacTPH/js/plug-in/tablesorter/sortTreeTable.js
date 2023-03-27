var findSortKey = function($cell) {
    return $cell.find('.sort-key').text().toUpperCase() + ' ' +
        $cell.text().toUpperCase();
};

jQuery(document).ready(function () {

    var $th = jQuery('thead > tr > th');

    $th.each(function(column, elt) {

        jQuery(this).addClass('sortable').click(function() {

            var sortDirection = jQuery(this).is('.sorted-asc') ? -1 : 1;

            var $tbodies = jQuery(this).parent().parent().parent()
                .find('.sortable-row').parent().get();
            jQuery.each($tbodies, function(index, tbody) {
                var x = findSortKey(jQuery(tbody).find('tr > td').eq(column));
                var z = ~~(x); // if integer, z == x
                tbody.sortKey = (z == x) ? z : x;
            });

            $tbodies.sort(function(a, b) {
                if (a.sortKey < b.sortKey) {return -sortDirection;}
                if (a.sortKey > b.sortKey) { return sortDirection;}
                return 0;
            });

            jQuery.each($tbodies, function(index, tbody) {
                jQuery('#my-tasks').append(tbody);
                tbody.sortKey = null;
            });

            jQuery('th').removeClass('sorted-asc sorted-desc');

            var $sortHead = jQuery('th').filter(':nth-child(' + (column + 1) + ')');
            if (sortDirection == 1){
                $sortHead.addClass('sorted-asc');
            }
            else {
                $sortHead.addClass('sorted-desc');
            }

            jQuery('td').removeClass('sorted')
                .filter(':nth-child(' + (column + 1) + ')').addClass('sorted');

        });
    });
});